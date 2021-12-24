package one_week_reparation_kit.day_02

/*
 * Complete the 'diagonalDifference' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts 2D_INTEGER_ARRAY arr as parameter.
 */

fun diagonalDifference(arr: Array<Array<Int>>): Int {
    // Write your code here
    var leftToRightDiagonal = 0
    var rightToLeftDiagonal = 0

    for (x in 0 until arr.size) {
        rightToLeftDiagonal += arr[x][arr.size - 1 - x]
        leftToRightDiagonal += arr[x][x]
    }
    return Math.abs(leftToRightDiagonal - rightToLeftDiagonal)
}