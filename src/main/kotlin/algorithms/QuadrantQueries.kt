package algorithms

import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream


class SegmentTree(private val min: Int, private val max: Int) {
    private val center: Int = (min + max) / 2
    private var left: SegmentTree? = null
    private var right: SegmentTree? = null
    private var count = 0
    private val leaf: Boolean = min == max

    fun add(n: Int) {
        count++
        if (leaf) {
            return
        } else if (n <= center) {
            if (left == null) left = SegmentTree(min, center)
            left!!.add(n)
        } else {
            if (right == null) right = SegmentTree(center + 1, max)
            right!!.add(n)
        }
    }

    fun countInRange(m: Int, n: Int): Int {
        if (min >= m && max <= n) {
            return count
        }
        var sum = 0
        if (left != null && m <= center) {
            sum += left!!.countInRange(m, n)
        }
        if (right != null && n > center) {
            sum += right!!.countInRange(m, n)
        }
        return sum
    }

    fun deleteRange(results: ArrayList<SegmentTree?>, m: Int, n: Int): Boolean {
        if (min >= m && max <= n) {
            results.add(this)
            return true
        }
        val origSize = results.size
        if (left != null && m <= center) {
            if (left!!.deleteRange(results, m, n) || left!!.count == 0) {
                left = null
            }
        }
        if (right != null && n > center) {
            if (right!!.deleteRange(results, m, n) || right!!.count == 0) {
                right = null
            }
        }
        if (results.size > origSize) {
            recalculateCount()
        }
        return false
    }

    fun merge(s: SegmentTree?) {
        if (s!!.min == min && s.max == max) {
            if (left != null && s.left != null) {
                left!!.merge(s.left)
            } else {
                left = s.left
            }
            if (right != null && s.right != null) {
                right!!.merge(s.right)
            } else {
                right = s.right
            }
        } else if (s.min == min && s.max == center && left == null) {
            left = s
        } else if (s.min == center + 1 && s.max == max) {
            right = s
        } else if (s.center <= center) {
            if (left == null) {
                left = SegmentTree(min, center)
            }
            left!!.merge(s)
        } else {
            if (right == null) {
                right = SegmentTree(center + 1, max)
            }
            right!!.merge(s)
        }
        count += s.count
    }

    private fun recalculateCount() {
        count = if (leaf) {
            1
        } else {
            (if (left != null) left!!.count else 0) + if (right != null) right!!.count else 0
        }
    }
}

class Quadrants(nPoints: Int) {
    private val quadrants: Array<SegmentTree?> = arrayOfNulls(4)
    private val tmp1: ArrayList<SegmentTree?>
    private val tmp2: ArrayList<SegmentTree?>
    private val out: PrintStream

    init {
        for (i in 0..3) {
            quadrants[i] = SegmentTree(1, nPoints)
        }
        tmp1 = ArrayList(20)
        tmp2 = ArrayList(20)
        out = PrintStream(BufferedOutputStream(System.out, 8192))
    }

    fun add(x: Int, y: Int, i: Int) {
        val q: Int = if (x >= 0 && y >= 0) 0 else if (x <= 0 && y >= 0) 1 else if (x <= 0 && y <= 0) 2 else 3
        quadrants[q]!!.add(i)
    }

    fun swapPointsInRange(q1: Int, q2: Int, m: Int, n: Int) {
        tmp1.clear()
        quadrants[q1]!!.deleteRange(tmp1, m, n)
        tmp2.clear()
        quadrants[q2]!!.deleteRange(tmp2, m, n)
        if (tmp2.isNotEmpty()) {
            val iter: Iterator<SegmentTree?> = tmp2.iterator()
            while (iter.hasNext()) {
                quadrants[q1]!!.merge(iter.next())
            }
        }
        if (tmp1.isNotEmpty()) {
            val iter: Iterator<SegmentTree?> = tmp1.iterator()
            while (iter.hasNext()) {
                quadrants[q2]!!.merge(iter.next())
            }
        }
    }

    fun mirrorX(m: Int, n: Int) {
        swapPointsInRange(0, 3, m, n)
        swapPointsInRange(1, 2, m, n)
    }

    fun mirrorY(m: Int, n: Int) {
        swapPointsInRange(0, 1, m, n)
        swapPointsInRange(2, 3, m, n)
    }

    fun flush() {
        out.flush()
    }

    fun count(m: Int, n: Int) {
        out.println(
            quadrants[0]!!.countInRange(m, n).toString() + " " + quadrants[1]!!.countInRange(
                m,
                n
            ) + " " + quadrants[2]!!.countInRange(m, n) + " " + quadrants[3]!!.countInRange(m, n)
        )
    }
}


fun main(args: Array<String>) {
    val bufferedReader = BufferedReader(InputStreamReader(System.`in`))
    val nPoints = bufferedReader.readLine().toInt()
    val quadrants = Quadrants(nPoints)
    for (i in 1..nPoints) {
        val line = bufferedReader.readLine()
        val spc = line.indexOf(' ')
        val x = if (line[0] == '-') -1 else 1
        val y = if (line[spc + 1] == '-') -1 else 1
        quadrants.add(x, y, i)
    }
    val nQueries = bufferedReader.readLine().toInt()
    for (i in 0 until nQueries) {
        val line = bufferedReader.readLine()
        val cmd = line[0]
        val spc = line.indexOf(' ', 2)
        val m = line.substring(2, spc).toInt()
        val n = line.substring(spc + 1).toInt()
        when (cmd) {
            'X' -> quadrants.mirrorX(m, n)
            'Y' -> quadrants.mirrorY(m, n)
            'C' -> quadrants.count(m, n)
        }
    }
    quadrants.flush()
}