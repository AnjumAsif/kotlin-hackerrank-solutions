package algorithms

import java.util.*

/*
 * Complete the 'activityNotifications' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER_ARRAY expenditure
 *  2. INTEGER d
 */

fun activityNotifications(expenditure: Array<Int>, d: Int): Int {
    if (d >= expenditure.size)
        return 0

    var notifications = 0
    val countArray = Array(201) { 0 }
    val queue = LinkedList<Int>()

    for (i in 1 until expenditure.size) {
        queue.add(expenditure[i - 1])
        countArray[expenditure[i - 1]]++
        if (i > d) {
            countArray[queue.poll()]--
        } else if (i < d) {
            continue
        }
        if (expenditure[i] >= 2 * median(countArray, d))
            notifications++
    }
    return notifications
}

fun median(countArray: Array<Int>, size: Int): Float {
    val odd = size and 1 == 1
    var cumulative = 0
    var result = 0f
    if (odd) {
        for (i in countArray.indices) {
            cumulative += countArray[i]
            if (cumulative > size / 2) {
                result = i.toFloat()
                break
            }
        }
    } else {
        var firstFound = false
        for (i in countArray.indices) {
            cumulative += countArray[i]
            if (cumulative - 1 >= size / 2 - 1) {
                if (!firstFound) {
                    result += i
                    firstFound = true
                }
                if (cumulative - 1 >= size / 2) {
                    result += i
                    break
                }
            }
        }
        result /= 2f
    }
    return result
}