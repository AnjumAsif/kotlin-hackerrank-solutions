package one_week_reparation_kit.day_02

/*
 * Complete the 'countingSort' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts INTEGER_ARRAY arr as parameter.
 */

fun countingSort(arr: Array<Int>): Array<Int> {
    // Write your code here
    val size = arr.size
    val result = IntArray(size)

    for (i in 0 until size) {
        val counter = result[arr[i]]
        if (counter == 0) result[arr[i]] = 1
        else result[arr[i]]++
    }

    return result.take(100).toTypedArray()
}