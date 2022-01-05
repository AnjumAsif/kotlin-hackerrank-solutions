package algorithms

import kotlin.math.abs

/*
 * Complete the 'diagonalDifference' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts 2D_INTEGER_ARRAY arr as parameter.

 * Explanation: We need to calculate the sums across the two diagonals of a square matrix.
 * First diagonal of the matrix, row index = column index.
 * The second diagonal is at column  for each row .
 * Loop through the rows, summing both values as increments.
 */

fun diagonalDifference(arr: Array<Array<Int>>): Int {
    val leftDiagonal = arr.mapIndexed { index, row -> row[index] }.sum()
    val rightDiagonal = arr.mapIndexed { index, row -> row[row.size - 1 - index] }.sum()
    return Math.abs(leftDiagonal - rightDiagonal)
}