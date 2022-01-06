package algorithms

import java.lang.Integer.max

/*
 * Complete the 'nonDivisibleSubset' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER k
 *  2. INTEGER_ARRAY s
 */

fun nonDivisibleSubset(k: Int, s: Array<Int>): Int {
    val count = Array(k) { 0 }
    for (x in s) count[x % k]++
    var result = 0
    for (i in 1..((k - 1) / 2)) {
        result += max(count[i], count[k - i])
    }
    if (count[0] != 0) result++
    if (k % 2 == 0 && count[k / 2] != 0) result++
    return result
}

