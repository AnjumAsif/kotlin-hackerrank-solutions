package data_structures

fun hourglassSum(arr: Array<Array<Int>>): Int {
    var maxSum = Int.MIN_VALUE
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            val sum =
                arr[i][j] + arr[i][j + 1] + arr[i][j + 2] + arr[i + 1][j + 1] + (arr[i + 2][j] + arr[i + 2][j + 1] + arr[i + 2][j + 2])
            maxSum = Math.max(maxSum, sum)
        }
    }
    return maxSum
}