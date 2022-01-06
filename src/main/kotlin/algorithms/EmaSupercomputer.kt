package algorithms

import kotlin.math.abs

/*
 * Complete the 'twoPluses' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts STRING_ARRAY grid as parameter.
 */
fun twoPluses(grid: Array<String>): Int {
    val good = 'G'
    val h = grid.size
    val w = grid[0].length
    val pluses = mutableListOf<Plus>()
    for (r in 0 until h) {
        for (c in 0 until w) {
            if (grid[r][c] == good) {
                var sideLen = 0
                while (true) {
                    pluses.add(Plus(r, c, sideLen))
                    sideLen++
                    if (r - sideLen < 0 || grid[r - sideLen][c] != good) {
                        break
                    }
                    if (r + sideLen >= h || grid[r + sideLen][c] != good) {
                        break
                    }
                    if (c - sideLen < 0 || grid[r][c - sideLen] != good) {
                        break
                    }
                    if (c + sideLen >= w || grid[r][c + sideLen] != good) {
                        break
                    }
                }
            }
        }
    }

    if (pluses.isEmpty()) return 0

    var max = 0
    for (i in 0 until pluses.size) {
        val first = pluses[i]
        for (j in 0 until i) {
            val second = pluses[j]
            if (!first.isOverlapping(second)) {
                val area = first.area * second.area
                if (area > max) {
                    max = area
                }
            }
        }
    }
    return max
}

data class Plus(val row: Int, val column: Int, val sideLength: Int)

val Plus.area: Int
    get() = 1 + sideLength * 4

fun Plus.isOverlapping(other: Plus): Boolean {
    if (row == other.row) {
        return abs(column - other.column) <= (sideLength + other.sideLength)
    }
    if (column == other.column) {
        return abs(row - other.row) <= (sideLength + other.sideLength)
    }
    return (abs(other.column - column) <= other.sideLength && abs(other.row - row) <= sideLength)
            || (abs(other.row - row) <= other.sideLength && abs(other.column - column) <= sideLength)
}

