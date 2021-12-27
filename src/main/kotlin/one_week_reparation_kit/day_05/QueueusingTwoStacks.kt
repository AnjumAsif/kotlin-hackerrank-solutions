package one_week_reparation_kit.day_05

import java.util.*

//Queue using Two Stacks Version 1
fun main(args: Array<String>) {
    /* Enter your code here. Read input from STDIN. Print output to STDOUT. */
    val q = LinkedList<String>();
    for (i in 1..readLine()!!.toInt()) {
        val line: String = readLine()!!
        when (line.get(0)) {
            // Push
            '1' -> {
                q.add(line.substring(2))
            }
            // Pop
            '2' -> {
                q.remove()
            }
            // Peek
            '3' -> {
                println(q.peek())
            }
        }
    }
}
