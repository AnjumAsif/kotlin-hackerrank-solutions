package data_structures

import java.util.*

private val buckets = LongArray(1000001)

private fun reportQuery(a: Int): Long {
    var total: Long = 0
    var i = a
    while (i > 0) {
        total += buckets[i]
        i -= i and i.inv() + 1
    }
    return total
}

private fun reportQuery(a: Int, b: Int): Long {
    return reportQuery(b) - if (a == 1) 0 else reportQuery(a - 1)
}

private fun adjust(k: Int, v: Int) {
    var k = k
    while (k < buckets.size) {
        buckets[k] += v.toLong()
        k += k and k.inv() + 1
    }
}

private fun update(pos: Int, M: Int, plus: Int) {
    var pos = pos
    val limit = 1000000 //1 million
    for (i in 1..50) {
        val back = pos
        for (j in 1..3) {
            if (j == 3) {
                adjust(pos, M * 998) // add M water balloons at bucket pos
            } else {
                adjust(pos, M)
            }
            var interval = 1
            while (true) {
                pos += pos and pos.inv() + 1
                if (pos > limit) break
                if (j == 3) {
                    buckets[pos] += (M * 998 * interval).toLong()
                } else buckets[pos] += (M * interval).toLong() // add M water balloons at bucket pos
                interval++
            }
            pos -= limit
        }
        pos = back + plus
        if (pos > limit) pos -= limit
    }
}

fun main(args: Array<String>) {
    val numberOfQueries = readLine()!!.trim().toInt()
    val output = StringBuilder()
    for (i in 0 until numberOfQueries) {
        val str = StringTokenizer(readLine())
        if (str.nextToken() == "U") {
            update(str.nextToken().toInt(), str.nextToken().toInt(), str.nextToken().toInt())
        } else {
            output.append(reportQuery(str.nextToken().toInt(), str.nextToken().toInt())).append("\n")
        }
    }
    print(output.toString())
}