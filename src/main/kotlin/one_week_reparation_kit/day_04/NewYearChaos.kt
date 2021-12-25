package one_week_reparation_kit.day_04

/*
 * Complete the 'minimumBribes' function below.
 *
 * The function accepts INTEGER_ARRAY q as parameter.
 */


//Version 1: Native approach using nested loop
fun minimumBribes(q: Array<Int>): Unit {
    var minimumBribes = 0
    for (i in q.size - 1 downTo 0) {
        if (q[i] - (i + 1) > 2) {
            println("Too chaotic")
            return
        }
        for (j in maxOf(0, q[i] - 2) until i)
            if (q[j] > q[i])
                minimumBribes++
    }
    println(minimumBribes)
}