package algorithms

import java.io.*
import java.util.*

fun main() {
    val out = PrintWriter(System.out)
    solveTrainingTheArmy(Input(BufferedReader(InputStreamReader(System.`in`))), out)
    out.close()
}

fun solveTrainingTheArmy(input: Input, out: PrintWriter) {
    val n = input.nextInt()
    val m = input.nextInt()
    val gn = n + m + 2
    val s = n + m
    val t = n + m + 1
    val g = Array(gn) { IntArray(gn) }
    for (i in 0 until n) {
        g[s][i] = input.nextInt()
        g[i][t] = 1
    }
    for (it in 0 until m) {
        val aSet = input.nextInt()
        for (i in 0 until aSet) {
            val a = input.nextInt() - 1
            g[a][n + it] = 1
        }
        val bSet = input.nextInt()
        for (i in 0 until bSet) {
            val b = input.nextInt() - 1
            g[n + it][b] = 1
        }
    }
    val col = BooleanArray(gn)
    var answer = 0
    while (dfs(s, t, g, col)) {
        answer++
        Arrays.fill(col, false)
    }
    out.println(answer)
}

private fun dfs(s: Int, t: Int, g: Array<IntArray>, col: BooleanArray): Boolean {
    if (s == t) {
        return true
    }
    if (col[s]) {
        return false
    }
    col[s] = true
    for (j in g.indices) {
        if (g[s][j] > 0 && dfs(j, t, g, col)) {
            g[s][j]--
            g[j][s]++
            return true
        }
    }
    return false
}

class Input(private var input: BufferedReader) {
    private var sb = StringBuilder()

    operator fun next(): String? {
        sb.setLength(0)
        while (true) {
            val c = input.read()
            if (c == -1) {
                return null
            }
            if (" \n\r\t".indexOf(c.toChar()) == -1) {
                sb.append(c.toChar())
                break
            }
        }
        while (true) {
            val c = input.read()
            if (c == -1 || " \n\r\t".indexOf(c.toChar()) != -1) {
                break
            }
            sb.append(c.toChar())
        }
        return sb.toString()
    }

    fun nextInt(): Int {
        return next()!!.toInt()
    }

    fun nextLong(): Long {
        return next()!!.toLong()
    }

    fun nextDouble(): Double {
        return next()!!.toDouble()
    }
}