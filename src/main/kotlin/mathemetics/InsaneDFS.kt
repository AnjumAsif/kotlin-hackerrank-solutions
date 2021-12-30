package mathemetics

import java.util.*

class InsaneDFS {
    val QUESTION_MARK = "?"
    val M = 1000000007
    val L = 100000
    val INVS = LongArray(L + 1)

    private fun init() {
        for (i in 1..L) {
            INVS[i] = solve(i).toLong()
        }
    }

    fun isQuestionMark(s: String): Boolean {
        return QUESTION_MARK == s
    }

    /** Assume 1 <= a < M and (a, M) = 1.  Return b such that 1 <= b < M and a*b = 1(mod M).  */
    private fun solve(a: Int): Int {
        //assert a >= 1 && a < M;
        var t = 0
        var r = M
        var newt = 1
        var newr = a
        while (newr != 0) {
            val q = r / newr
            var temp = t - q * newt
            t = newt
            newt = temp
            temp = r - q * newr
            r = newr
            newr = temp
        }
        //assert (a * result) % M == 1 && result >= 1 && result < M;
        return if (t < 0) t + M else t
    }

    fun add(b: Long, c: Long): Long {
        //assert b >= 0 && b < M;
        //assert c >= 0 && c < M;
        return (b + c) % M
    }

    fun m(b: Long, c: Long): Long {
        //assert b >= 0 && b < M;
        //assert c >= 0 && c < M;
        return b * c % M
    }

    private fun d(b: Long, c: Long): Long {
        //assert b >= 0 && b < M;
        //assert c > 0 && c <= L;
        return m(b, INVS[c.toInt()])
    }

    private fun numSeqs(start: Int, end: Int, numQ: Int): Long {
        // Say a node N is an owner of a node N' iff N = N' or N is an ancestor of N'.
        // Say a node N'' is the common owner of nodes N and N' if it is the node with
        // maximum depth among all nodes that own both N and N'.
        // [min, max] is the range of the possible depths of the common owner of start and end.
        val min = 0.coerceAtLeast(end - numQ - 1)
        val max = start.coerceAtMost(end - 1)

        // For each i, sum is incremented by the number of depth sequences that correspond to trees
        // with start and end's common owner having depth i.  Let this common owner be N.  Let its
        // chain down to start be A_1, ..., A_p (where A_1 is N, A_p is start, and p >= 1).  Let its
        // chain down to end be B_0, ..., B_q (where B_0 is N, B_q is end, and q >= 1).  Then the
        // subsequence from start to end (inclusive) can be decomposed as
        // [start = A_p]
        // [descendants of A_p]
        // [descendants of A_(p-1) from its children after A_p]
        // ...
        // [descendants of A_2 from its children after A_3]
        // [descendants of A_1=B_0 from its children between A_2 and B_1]
        // [B_1]
        // [descendants of B_1 from its children before B_2]
        // ...
        // [B_(q-1)]
        // [descendants of B_(q-1) from its children before B_q]
        // [end = B_q]
        //
        // The number of possibilities for each of the [descendants] is represented by C(x),
        // where C(x) is the Catalan number generating function.
        // There are k = p + q of these [descendants].
        // The sum of the sizes of the [descendants] is a = numQ - (q - 1).
        // Hence the number of total possibilities is the coefficient of x^a in C(x)^k.
        // It is given on p.11 of http://people.brandeis.edu/~gessel/homepage/papers/enum.pdf .
        // It is easier to compute when rewritten as (k/a)*[c choose a-1],
        // where c = 2a + k - 1 turns out to be constant for all values of i.
        // Summary: Sum (k/a)*[c choose a-1] over all values of i in [min, max].
        val c = start - end + 2 * numQ + 1
        var sum = 0L
        var coeff = 0L
        var firstCoeff = true
        for (i in min..max) {
            val k = start + end - 2 * i // Positive because i <= max <= both start and end-1
            val a = numQ - end + i + 1 // Non-negative because i >= min >= end - numQ - 1
            assert(a in 0..c)
            assert(c == 2 * a + k - 1)
            if (a == 0) {  // Degenerate case
                sum = add(sum, 1L)
                continue
            }
            if (firstCoeff) {
                firstCoeff = false
                coeff = 1L
                // After this for loop, coeff is [c choose a-1] mod M
                for (j in 1 until a) {
                    coeff = m(coeff, (c - j + 1).toLong())
                    coeff = d(coeff, j.toLong())
                }
            } else {
                // coeff used to be [c choose a-2] mod M -- turn it into [c choose a-1] mod M
                coeff = m(coeff, (c - a + 2).toLong())
                coeff = d(coeff, (a - 1).toLong())
            }
            // Add (k/a)*[c choose a-1] mod M
            sum = add(sum, d(m(coeff, k.toLong()), a.toLong()))
        }
        return sum
    }

    fun main(args: Array<String>) {
        init()
        val input = Scanner(System.`in`)
        val n = input.nextInt()
        input.nextLine()
        val tokens = input.nextLine().split(" ").toTypedArray()
        if (!isQuestionMark(tokens[0]) && "0" != tokens[0]) {
            println("0")
            return
        }
        if (n > 1 && !isQuestionMark(tokens[1]) && "1" != tokens[1]) {
            println("0")
            return
        }
        var product = 1L
        var prev = 1
        var numQ = 0
        for (i in 2 until n) {
            if (isQuestionMark(tokens[i])) {
                numQ++
            } else {
                val curr = Integer.valueOf(tokens[i])
                product = m(product, numSeqs(prev, curr, numQ))
                if (product == 0L) {
                    println("0")
                    return
                }
                prev = curr
                numQ = 0
            }
        }
        product = m(product, numSeqs(prev, 1, numQ))
        println(product)
    }
}