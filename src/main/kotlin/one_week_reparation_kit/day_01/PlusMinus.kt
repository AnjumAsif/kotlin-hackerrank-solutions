package one_week_reparation_kit.day_01

/*
 * Complete the 'plusMinus' function below.
 *
 * The function accepts INTEGER_ARRAY arr as parameter.
 */

fun plusMinus(arr: Array<Int>): Unit {
    var negative = 0
    var zero = 0
    var positive = 0

    val arraySize = arr.size

    arr.forEach { it ->
        when {
            it == 0 -> zero++
            it < 0 -> negative++
            else -> positive++
        }
    }

    println(positive.toFloat() / arraySize)
    println(negative.toFloat() / arraySize)
    println(zero.toFloat() / arraySize)
}