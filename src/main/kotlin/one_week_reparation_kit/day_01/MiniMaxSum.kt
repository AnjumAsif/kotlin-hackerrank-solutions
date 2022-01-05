package one_week_reparation_kit.day_01

/*
 * Complete the 'miniMaxSum' function below.
 *
 * The function accepts INTEGER_ARRAY arr as parameter.
 *
 * Explanation:
 * Minimize sum = Sum - max
 * Maximize sum = sum - min
 *
 */

fun miniMaxSum(arr: Array<Int>): Unit {
    val sum = arr.map { it.toLong() }.sum()
    println("${sum - arr.maxOrNull()!!} ${sum - arr.minOrNull()!!}")
}