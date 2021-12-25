package one_week_reparation_kit.day_06

import java.util.*

fun main(args: Array<String>) {
    val undo: Stack<String> = Stack<String>()
    var current = ""
    val operatorType = readLine()!!.toInt()
    val output = StringBuilder()

    for (i in 1..operatorType) {
        val currentLine = readLine()!!
        when (currentLine[0]) {
            '1' -> {
                undo.push(current)
                current += currentLine.substring(2)
            }
            '2' -> {
                undo.push(current)
                val count = currentLine.substring(2).toInt()
                current = current.substring(0, current.length - count)
            }
            '3' -> {
                val index = currentLine.substring(2).toInt() - 1
                output.append(current[index]).append(System.lineSeparator())
            }
            '4' -> {
                current = undo.pop()
            }
        }
    }
    print(output)
}