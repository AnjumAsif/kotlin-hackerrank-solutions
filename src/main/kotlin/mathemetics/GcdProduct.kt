package mathemetics

import java.util.*

/*
 * Complete the 'solve' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER n
 *  2. INTEGER m
 */

const val M = 1000000007
val p = BooleanArray(15000000 + 1)

private fun solve(n: Int, m: Int): Long {
    generatePrimes()
    val min = n.coerceAtMost(m)
    var sol: Long = 1
    for (i in 0..min) {
        if (!p[i]) continue
        var pow: Long = 0
        var div = i.toLong()
        while (n / div * (m / div) > 0) {
            pow += n / div * (m / div)
            div *= i.toLong()
        }
        sol = sol * modPow(i.toLong(), pow) % M
    }
    return sol
}

fun generatePrimes() {
    Arrays.fill(p, true)
    p[1] = false
    p[0] = p[1]
    for (i in 2 until p.size) if (p[i]) {
        var j = i + i
        while (j < p.size) {
            p[j] = false
            j += i
        }
    }
}

private fun modPow(base: Long, pow: Long): Int {
    var pow = pow
    var t: Long = 1
    var p = base
    while (pow > 0) {
        if (pow and 1 == 1L) t = t * p % M
        p = p * p % M
        pow = pow ushr 1
    }
    return t.toInt()
}