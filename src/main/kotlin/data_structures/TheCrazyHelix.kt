package data_structures

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

fun main(args: Array<String>) {
    val inputReader = BufferedReader(InputStreamReader(System.`in`))
    val firstLine = StringTokenizer(inputReader.readLine().trim { it <= ' ' })
    val arrayLength = firstLine.nextToken().toInt()
    val nActions = firstLine.nextToken().toInt()
    val helixArray = IntArray(arrayLength)
    for (i in 0 until arrayLength) {
        helixArray[i] = i + 1
    }
    val helix = CrazyHelix(helixArray)
    val result = StringBuilder()

    for (i in 0 until nActions) {
        val line = StringTokenizer(inputReader.readLine().trim { it <= ' ' })
        when (line.nextToken().toInt()) {
            1 -> {
                val first = line.nextToken().toInt()
                val last = line.nextToken().toInt()
                helix.rotate(first, last)
            }
            2 -> {
                val searchItem = line.nextToken().toInt()
                val position = helix.getPositionOfItem(searchItem)
                result.append(String.format("element %d is at position %d\n", searchItem, position))
            }
            3 -> {
                val pos = line.nextToken().toInt()
                val item = helix.getItemAtPosition(pos)
                result.append(String.format("element at position %d is %d\n", pos, item))
            }
        }
    }
    println(result.toString())
}

class CrazyHelix(private val helixArray: IntArray) {
    fun rotate(first: Int, last: Int) {
        val rotationSize = (last - first + 1) / 2
        for (i in 0 until rotationSize) {
            val initialIndex = first - 1 + i
            val rotatedIndex = last - 1 - i
            val originalValue = helixArray[initialIndex]
            helixArray[initialIndex] = helixArray[rotatedIndex]
            helixArray[rotatedIndex] = originalValue
        }
    }

    fun getItemAtPosition(position: Int): Int {
        return helixArray[position - 1]
    }

    fun getPositionOfItem(item: Int): Int {
        for (i in helixArray.indices) {
            if (helixArray[i] == item) {
                return i + 1
            }
        }
        return -1
    }
}