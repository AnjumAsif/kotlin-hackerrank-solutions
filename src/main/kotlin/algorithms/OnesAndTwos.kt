package algorithms

import java.util.*
import kotlin.io.*
import kotlin.ranges.*
import kotlin.text.*

/*
 * Complete the 'onesAndTwos' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER a
 *  2. INTEGER b
 */

fun onesAndTwos(a: Int, b: Int): Int {
    // Write your code here

    var ans: Long = 0
    if (b == 0) {
        return a
    } else if (a == 0) {
        for (d in 0..b) {
            val temp = dp1[b][d]
            ans += temp
        }
        ans %= sp

    } else {
        for (d in 0 until b) {
            var temp = dp1[b - 1][d]
            val mult: Long = 2
            temp *= mult
            ans += temp
        }
        ans %= sp
        for (d in 0..31) {
            for (e in d + 1..31) {
                val repValue = list[b][d][e]
                val temp = repValue * Math.min((a + 1).toLong(), pow2[e] - pow2[d])
                ans += temp
                ans %= sp
            }
        }
        ans %= sp
        val temp = all[b] * (a + 1)
        ans += temp
        ans += (a + 2).toLong()
        ans %= sp
    }
    return ans.toInt()

}

val dp = Array(1001) { LongArray(1001) }
val dp1 = Array(1001) { LongArray(1001) }
val dpsum = Array(1001) { LongArray(1001) }
val sp: Long = 1000000007
val pow2 = LongArray(32)

val list = Array(1001) { Array(32) { LongArray(32) } }
val all = LongArray(1001)

fun build() {

    for (i in 0..1000) {
        Arrays.fill(dp[i], 0)
    }

    pow2[0] = 1
    for (i in 1..31) {
        pow2[i] = pow2[i - 1] * 2
    }
    dp[1][1] = 1
    for (i in 2..1000) {
        for (j in 1..i) {
            var temp: Long = 0
            for (k in j + 1 until i) {
                temp += dp[i - j][k]
                if (temp >= sp) {
                    temp -= sp
                }
            }
            if (i == j) {
                temp++
                if (temp >= sp) {
                    temp -= sp
                }
            }
            dp[i][j] = temp
        }
    }
    for (k in 0..1000) {
        Arrays.fill(dp1[k], 0)
    }
    for (i in 0..1000) {
        dpsum[i][0] = 0
    }
    for (i in 0..1000) {
        for (j in 1..1000) {
            dpsum[i][j] = dpsum[i][j - 1] + dp[i][j]
            if (dpsum[i][j] >= sp) {
                dpsum[i][j] -= sp
            }
        }
    }
    for (k in 1..1000) {
        for (i in k..1000) {
            if (i == k) {
                dp1[i][k] = 1
            } else {
                dp1[i][k] = dp1[i - 1][k] + dp[i][k]
                if (dp1[i][k] >= sp) {
                    dp1[i][k] -= sp
                }
            }
        }
    }

    for (i in 0..1000) {
        for (j in 0..31) {
            for (k in 0..31) {
                list[i][j][k] = 0
            }
        }
        all[i] = 0
        for (j in 1..Math.min(i / 2, 500)) {
            for (k in j + 1..i - j) {
                var repValue: Long = 0
                if (j + k == i) {
                    repValue++
                }
                repValue += dpsum[i - j - k][i - j - k] - dpsum[i - j - k][k]
                if (repValue < 0) {
                    repValue += sp
                }
                if (repValue >= sp) {
                    repValue -= sp
                }
                if (k < 32) {
                    list[i][j][k] = repValue
                } else {
                    all[i] += repValue
                }
            }
        }
        all[i] %= sp
    }
}

fun main(args: Array<String>) {
    val t = readLine()!!.trim().toInt()
    build()
    for (tItr in 1..t) {
        val first_multiple_input = readLine()!!.trimEnd().split(" ")

        val a = first_multiple_input[0].toInt()

        val b = first_multiple_input[1].toInt()

        val result = onesAndTwos(a, b)

        println(result)
    }
}
