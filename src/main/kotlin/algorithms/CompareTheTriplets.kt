package algorithms

/*
 * Complete the 'compareTriplets' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts following parameters:
 *  1. INTEGER_ARRAY a
 *  2. INTEGER_ARRAY b
 */

fun compareTriplets(a: Array<Int>, b: Array<Int>): Array<Int> {
    var aliceScore = 0
    var bobScore = 0
    for (index in 0..a.lastIndex) {
        if (a[index] > b[index]) aliceScore++
        else if (a[index] < b[index]) bobScore++
    }
    return arrayOf(aliceScore, bobScore)
}