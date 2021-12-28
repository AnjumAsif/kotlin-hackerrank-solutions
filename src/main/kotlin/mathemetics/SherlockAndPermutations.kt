package mathemetics

import java.math.BigInteger

/*
 * Complete the 'solve' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER n
 *  2. INTEGER m
 */

fun solve(n: Int, m: Int): Int {
    val small = n.coerceAtMost(m - 1)
    val big = n.coerceAtLeast(m - 1)
    var result = BigInteger.ONE
    for (i in (big + 1) until (big + small + 1)) {
        result = result.multiply(i.toBigInteger())
    }
    for (i in 2 until small + 1) {
        result = result.divide(i.toBigInteger())
    }
    return result.mod(BigInteger("1000000007")).toInt()
}
