package data_structures

/*
 * Complete the 'rotateLeft' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts following parameters:
 *  1. INTEGER d
 *  2. INTEGER_ARRAY arr
 */

fun rotateLeft(d: Int, arr: Array<Int>): Array<Int> {
    // Write your code here
    if (d > arr.size || d == 0) return arr
    return arr.sliceArray(d..arr.lastIndex) + arr.sliceArray(0 until d)

}