package algorithms

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.*


class Robot {
    lateinit var br: BufferedReader
    lateinit var out: PrintWriter
    var st: StringTokenizer? = null
    var eof = false

    fun main(args: Array<String>) {

        br = BufferedReader(InputStreamReader(System.`in`))
        out = PrintWriter(System.out)
        solve()
        out.close()

        Robot()
    }

    class Node(var l: Int, var r: Int) {
        var left: Node? = null
        var right: Node? = null
        var min: Long
        val INF = Long.MAX_VALUE / 4

        init {
            min = INF
            if (r - l > 1) {
                val mid = l + r shr 1
                left = Node(l, mid)
                right = Node(mid, r)
            }
        }

        fun put(pos: Int, value: Long) {
            if (l == pos && pos + 1 == r) {
                min = min.coerceAtMost(value)
                return
            }
            (if (pos < left!!.r) left else right)!!.put(pos, value)
            min = left!!.min.coerceAtMost(right!!.min)
        }

        operator fun get(ql: Int, qr: Int): Long {
            if (ql >= qr || l >= qr || ql >= r) {
                return INF
            }
            return if (ql <= l && r <= qr) {
                min
            } else left!![ql, qr].coerceAtMost(right!![ql, qr])
        }
    }


    fun solve() {
        val n = nextInt()
        val m = IntArray(n)
        val p = IntArray(n)
        var sum: Long = 0
        for (i in 0 until n) {
            m[i] = nextInt()
            sum += m[i].toLong()
            p[i] = nextInt()
        }
        val root = Node(0, n)
        root.put(n - 1, 0)
        for (i in n - 2 downTo 0) {
            val to = (i + p[i]).coerceAtMost(n) + 1
            root.put(i, root[i + 1, to] + m[i])
        }
        out.println(sum - root[0, 1])
    }

    fun nextToken(): String? {
        while (st == null || !st!!.hasMoreTokens()) {
            try {
                st = StringTokenizer(br.readLine())
            } catch (e: Exception) {
                eof = true
                return null
            }
        }
        return st!!.nextToken()
    }


    fun nextInt(): Int {
        return nextToken()!!.toInt()
    }
}