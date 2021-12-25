package one_week_reparation_kit.day_05


//Queue using Two Stacks Version 2

fun main(args: Array<String>) {
    /* Enter your code here. Read input from STDIN. Print output to STDOUT. */
    val numberOfQueries = readLine()!!.toInt()
    val queue = Queue()
    (1..numberOfQueries).forEach {
        val queryType = readLine()!!.toString()
        when (queryType[0]) {
            '1' -> {
                val element = queryType.split(' ')[1].toInt()
                queue.enqeue(element)
            }
            '2' -> queue.dequeue()
            '3' -> queue.printFront()
        }
    }
}

class Queue {
    val queue = mutableListOf<Int>()

    fun enqeue(element: Int) {
        queue.add(element)
    }

    fun dequeue() {
        queue.removeAt(0)
    }

    fun printFront() {
        println(queue[0])
    }

    fun debug() {
        println("QUEUE: $queue")
    }
}