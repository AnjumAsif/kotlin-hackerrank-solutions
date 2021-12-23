package one_week_reparation_kit.day_01

/*
 * Complete the 'miniMaxSum' function below.
 *
 * The function accepts INTEGER_ARRAY arr as parameter.
 */

fun miniMaxSum(arr: Array<Int>): Unit {
    //To pass all test case, beware of integer overflow! Use 64-bit Integer
    val sortedArr = arr.sorted().map { it.toLong() }
    val total: Long = sortedArr.sum()
    println("${total - sortedArr.last()} ${total - sortedArr.first()}")
}