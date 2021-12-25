package one_week_reparation_kit.day_06

import java.util.*

/*
 * Complete the 'cookies' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER k
 *  2. INTEGER_ARRAY A
 */

fun cookies(k: Int, A: Array<Int>): Int {
    val sortedSweetnessValues = PriorityQueue<Int>()
    sortedSweetnessValues.addAll(A)
    var numerOfoperations = 0
    while (sortedSweetnessValues.peek() < k && sortedSweetnessValues.size > 1) {
        ++numerOfoperations
        sortedSweetnessValues.add(sortedSweetnessValues.poll() + sortedSweetnessValues.poll() * 2)
    }
    return if (sortedSweetnessValues.first() >= k) numerOfoperations else -1
}