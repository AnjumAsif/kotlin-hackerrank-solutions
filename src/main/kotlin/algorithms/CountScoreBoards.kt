package algorithms

import java.util.*

class CountScoreBoards {

/*
 * Complete the 'countScorecards' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts INTEGER_ARRAY s as parameter.
 */

    var MOD = 1000000007
    var maxN = 42
    lateinit var a: IntArray
    lateinit var minDiff: IntArray
    var longArrays = Array(maxN) { LongArray(maxN) }
    var ts = IntArray(maxN)
    var number = 0
    var m: Int = 0
    var N: Int = 0
    var pScores: Int = 0

    var memo = Array(maxN) { Array(maxN) { IntArray(maxN * maxN / 2) } }
    var memoCnt = 0
    var dp = Array(maxN) { Array(maxN) { IntArray(maxN * maxN / 2) } }

    fun recC(k: Int, last: Int, sum: Int): Int {
        if (k == m) {
            return if (pScores + sum == ts[N - 1]) 1 else 0
        }
        if (last >= N || sum > ts[N - 1]) {
            return 0
        }
        if (memo[k][last][sum] == memoCnt) {
            return dp.get(k).get(last).get(sum)
        }
        memo[k][last][sum] = memoCnt
        var result = recC(k, last + 1, sum)
        var sumR = sum
        var i = 1
        while (k + i <= m) {
            sumR += last
            if (sumR + minDiff.get(k + i) < ts[k + i - 1]) {
                break
            }
            result = ((result + longArrays.get(m - k).get(i) * recC(k + i, last + 1, sumR)) % MOD).toInt()
            ++i
        }
        dp.get(k).get(last)[sum] = result
        return result
    }

    fun countScorecards(s: Array<Int>): Int {
        build()
        N = s.size
        number = 0
        m = 0
        a = IntArray(maxN)
        for (i in 0 until N) {
            val x = s[i]
            if (x == -1) {
                m++
            } else {
                a[number++] = x
            }
        }
        minDiff = IntArray(m + 1)
        val nA = IntArray(number)
        System.arraycopy(a, 0, nA, 0, number)
        a = nA
        Arrays.sort(a)
        pScores = 0
        for (i in 0 until number) {
            pScores += a[i]
            if (ts[i] > pScores) {
                return 0
            }
        }
        for (i in 1..m) {
            var sum = 0
            minDiff[i] = 0
            for (j in 0 until number) {
                sum += a[j] - (i + j)
                minDiff[i] = Math.min(minDiff[i], sum)
            }
        }
        memoCnt++
        return recC(0, 0, 0)
    }

    fun build() {
        longArrays[0][0] = 1
        longArrays[1][0] = 1
        longArrays[1][1] = 1
        for (i in 2 until maxN) {
            longArrays[i][0] = 1
            for (j in 1..i) {
                longArrays[i][j] = (longArrays[i - 1][j] + longArrays[i - 1][j - 1]) % MOD
            }
        }
        for (i in 1 until maxN) {
            ts[i] = i * (i + 1) / 2
        }
    }

}