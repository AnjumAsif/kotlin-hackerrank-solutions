package one_week_reparation_kit.day_01

/*
 * Complete the 'plusMinus' function below.
 *
 * The function accepts INTEGER_ARRAY arr as parameter.
 */

fun plusMinus(arr: Array<Int>): Unit {
    val size = arr.size
    println(arr.count { it > 0 }.toDouble() / size)
    println(arr.count { it < 0 }.toDouble() / size)
    println(arr.count { it == 0 }.toDouble() / size)
}