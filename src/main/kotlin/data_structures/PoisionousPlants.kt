package data_structures

import java.util.*

/*
 * Complete the 'poisonousPlants' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts INTEGER_ARRAY p as parameter.
 */

fun poisonousPlants(p: Array<Int>): Int {
    // Write your code here
    val dayStack = Stack<Int>()
    val pesticideLevel = Array(p.size) { 0 }
    var result = 0
    for (i in p.size - 1 downTo 0) {
        var sum = 0
        while (!dayStack.empty() && p[i] < p[dayStack.peek()]) {
            val it = dayStack.pop()
            sum += if (sum + 1 > pesticideLevel[it]) 1 else pesticideLevel[it] - sum
        }
        dayStack.push(i)
        pesticideLevel[i] = sum
        result = result.coerceAtLeast(sum)
    }
    return result
}