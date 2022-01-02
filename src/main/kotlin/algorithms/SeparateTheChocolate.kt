package algorithms

import java.util.*
import kotlin.math.abs


class SeparateTheChocolate {

    class Cell(var piece: Int, var groupIndex: Int) {
        override fun toString(): String {
            return if (piece == 0) {
                "" + ('a'.toInt() + groupIndex).toChar()
            } else {
                "" + ('A'.toInt() + groupIndex).toChar()
            }
        }

        override fun equals(other: Any?): Boolean {
            val other = other as Cell?
            return piece == other!!.piece && groupIndex == other.groupIndex
        }

        override fun hashCode(): Int {
            return 31 * piece + groupIndex
        }

        fun copy(): Cell {
            return Cell(piece, groupIndex)
        }
    }

    class LineState {
        var cells: Array<Cell?>
        var isHidingGroup = false

        val isOfTwoPieces: Boolean
            get() {
                if (isHidingGroup && isUniform) return true
                for (i in cells.indices) {
                    if (cells[i]!!.groupIndex > 0) return false
                }
                return true
            }

        constructor(c: Array<Cell?>) {
            cells = c.clone()
        }

        constructor(template: Int, width: Int) {
            var template = template
            cells = arrayOfNulls(width)
            for (i in 0 until width) {
                cells[i] = Cell(template % 2, -1)
                template /= 2
            }
        }

        fun isMatch(constraint: IntArray): Boolean {
            for (i in cells.indices) {
                if (constraint[i] >= 0 && constraint[i] != cells[i]!!.piece) {
                    return false
                }
            }
            return true
        }

        fun countOnes(): Int {
            var c = 0
            for (i in cells.indices) {
                c += cells[i]!!.piece
            }
            return c
        }

        override fun toString(): String {
            val sb = StringBuilder()
            for (c in cells) {
                sb.append(c)
            }
            if (isHidingGroup) sb.append("!")
            return sb.toString()
        }

        override fun equals(other: Any?): Boolean {
            val other = other as LineState?
            return toString() == other.toString()
        }

        fun copy(): LineState {
            val cs = arrayOfNulls<Cell>(cells.size)
            for (i in cs.indices) {
                cs[i] = cells[i]!!.copy()
            }
            val copy = LineState(cs)
            copy.isHidingGroup = isHidingGroup
            return copy
        }

        val isUniform: Boolean
            get() {
                val p = cells[0]!!.piece
                for (i in 1 until cells.size) {
                    if (cells[i]!!.piece != p) return false
                }
                return true
            }

        fun revalidate() {
            var g0 = 20
            var g1 = 20
            for (i in cells.indices) {
                val g = cells[i]!!.groupIndex
                if (g >= 0) continue
                val p = cells[i]!!.piece
                if (p == 0) {
                    var j = i
                    while (j < cells.size && cells[j]!!.piece == p && cells[j]!!.groupIndex < 0) {
                        cells[j]!!.groupIndex = g0
                        j++
                    }
                    g0++
                } else {
                    var j = i
                    while (j < cells.size && cells[j]!!.piece == p && cells[j]!!.groupIndex < 0) {
                        cells[j]!!.groupIndex = g1
                        j++
                    }
                    g1++
                }
            }
            val m0 = IntArray(cells.size + 20)
            val m1 = IntArray(cells.size + 20)
            for (i in m0.indices) {
                m0[i] = -1
                m1[i] = -1
            }
            g0 = 0
            g1 = 0
            for (i in cells.indices) {
                val g = cells[i]!!.groupIndex
                if (g < 0) continue
                if (cells[i]!!.piece == 0) {
                    if (m0[g] >= 0) {
                        cells[i]!!.groupIndex = m0[g]
                    } else {
                        m0[g] = g0
                        cells[i]!!.groupIndex = g0
                        g0++
                    }
                } else {
                    if (m1[g] >= 0) {
                        cells[i]!!.groupIndex = m1[g]
                    } else {
                        m1[g] = g1
                        cells[i]!!.groupIndex = g1
                        g1++
                    }
                }
            }
        }

        override fun hashCode(): Int {
            var result = cells.contentHashCode()
            result = 31 * result + isHidingGroup.hashCode()
            result = 31 * result + isOfTwoPieces.hashCode()
            result = 31 * result + isUniform.hashCode()
            return result
        }
    }

    var T = 0
    var D = 1
    var U = -1

    var width = 0
    var height = 0
    var diff = 0
    lateinit var constraint: Array<IntArray>
    var status: MutableMap<String, LineState> = HashMap()
    var transfers: MutableMap<String, Set<String>> = HashMap()
    var counts: MutableMap<String, MutableMap<Int, Long?>> = HashMap()

    fun setSize(w: Int, h: Int, d: Int) {
        width = w
        height = h
        diff = d
    }

    fun createState(): List<LineState> {
        val states: MutableList<LineState> = ArrayList()
        var t = 0
        val cells = arrayOfNulls<Cell>(width)
        val way = IntArray(width + 1)
        val wayCount = IntArray(width + 1)
        val ways = Array(width + 1) {
            arrayOfNulls<Cell>(
                width + 1
            )
        }
        ways[0][0] = Cell(0, 0)
        ways[0][1] = Cell(1, 0)
        wayCount[0] = 2
        way[t] = -1
        while (true) {
            while (way[t] == wayCount[t] - 1) {
                if (t == 0) return states
                t--
            }
            way[t]++
            cells[t] = ways[t][way[t]]
            t++
            wayCount[t] = 0
            if (t < width) {
                val p = cells[t - 1]!!.piece
                ways[t][0] = Cell(p, cells[t - 1]!!.groupIndex) //same
                wayCount[t]++
                val gStack: MutableList<Cell?> = ArrayList()
                var g = -1
                for (i in 0 until t) {
                    val j = gStack.indexOf(cells[i])
                    if (j >= 0) {
                        while (gStack.size > j + 1) gStack.removeAt(gStack.size - 1)
                    } else {
                        gStack.add(cells[i])
                        if (cells[i]!!.piece == 1 - p) {
                            if (cells[i]!!.groupIndex > g) g = cells[i]!!.groupIndex
                        }
                    }
                }
                for (c in gStack) {
                    if (c!!.piece == 1 - p) {
                        ways[t][wayCount[t]] = c
                        wayCount[t]++
                    }
                }
                ways[t][wayCount[t]] = Cell(1 - p, g + 1)
                wayCount[t]++
            } else {
                states.add(LineState(cells))
            }
            way[t] = -1
        }
    }

    //to - template
    fun transfer(from: LineState, to: LineState?): LineState? {
        var from = from
        var to = to
        if (from.isHidingGroup) {
            if (width == 1 && from.cells[0]!!.piece == to!!.cells[0]!!.piece) {
                to = to.copy()
                to.isHidingGroup = true
                to.revalidate()
                return to
            }
            return null
        }
        for (i in 0 until from.cells.size - 1) {
            val p = from.cells[i]!!.piece
            if (p == from.cells[i + 1]!!.piece && p == to!!.cells[i]!!.piece && p == to.cells[i + 1]!!.piece) {
                return null //square 2x2
            }
        }
        from = from.copy()
        to = to!!.copy()
        for (i in from.cells.indices) {
            to.cells[i]!!.groupIndex = -1
        }
        for (i in from.cells.indices) {
            val p = from.cells[i]!!.piece
            val g1 = from.cells[i]!!.groupIndex
            val g2 = to.cells[i]!!.groupIndex
            if (p == to.cells[i]!!.piece && g1 != g2) {
                if (g2 >= 0) {
                    for (j in from.cells.indices) {
                        val ga = if (g1 < g2) g1 else g2
                        val gb = if (g1 > g2) g1 else g2
                        if (p == from.cells[j]!!.piece && gb == from.cells[j]!!.groupIndex) {
                            from.cells[j]!!.groupIndex = ga
                        }
                        if (p == to.cells[j]!!.piece && gb == to.cells[j]!!.groupIndex) {
                            to.cells[j]!!.groupIndex = ga
                        }
                    }
                } else {
                    to.cells[i]!!.groupIndex = g1
                    var j = i + 1
                    while (j < from.cells.size && to.cells[j]!!.piece == p) {
                        to.cells[j]!!.groupIndex = g1
                        j++
                    }
                    j = i - 1
                    while (j >= 0 && to.cells[j]!!.piece == p) {
                        to.cells[j]!!.groupIndex = g1
                        j--
                    }
                }
            }
        }
        val accounted: MutableSet<Cell?> = HashSet()
        for (i in from.cells.indices) {
            if (from.cells[i]!!.piece == to.cells[i]!!.piece) {
                accounted.add(from.cells[i])
            }
        }
        val unaccounted: MutableSet<Cell?> = HashSet()
        for (i in from.cells.indices) {
            if (!accounted.contains(from.cells[i]) && !unaccounted.contains(from.cells[i])) {
                unaccounted.add(from.cells[i])
            }
        }
        if (unaccounted.size > 1) return null
        to.revalidate()
        if (unaccounted.size == 1) {
            if (!to.isUniform) {
                return null
            } else {
                to.isHidingGroup = true
            }
        }
        return to
    }

    fun solve() {
        var p2 = 1
        for (i in 0 until width) p2 *= 2
        val states = createState()
        for (s in states) {
            status[s.toString()] = s
            if (s.isUniform) {
                val s1 = s.copy()
                s1.isHidingGroup = true
                status[s1.toString()] = s1
            }
        }
        for (s in status.values) {
            val ts: MutableSet<String> = HashSet()
            for (i in 0 until p2) {
                var t: LineState? = LineState(i, width)
                t = transfer(s, t)
                if (t != null) ts.add(t.toString())
            }
            transfers[s.toString()] = ts
        }
        for (i in 0 until p2) {
            val t = LineState(i, width)
            t.revalidate()
            if (!t.isMatch(constraint[0])) continue
            val v: MutableMap<Int, Long?> = HashMap()
            v[t.countOnes()] = 1L
            counts[t.toString()] = v
        }
        for (i in 0 until height - 1) {
            counts = addRow(counts, constraint[i + 1])
        }
        val sum = sum(counts, diff, width * height)
        println(sum)
    }

    fun addRow(counts: Map<String, MutableMap<Int, Long?>>, cs: IntArray): MutableMap<String, MutableMap<Int, Long?>> {
        val next: MutableMap<String, MutableMap<Int, Long?>> = HashMap()
        for (s in counts.keys) {
            val vs: Map<Int, Long?> = counts[s]!!
            for (n in transfers[s]!!) {
                val t = status[n]
                if (!t!!.isMatch(cs)) continue
                val dk = t.countOnes()
                if (!next.containsKey(n)) {
                    val v: MutableMap<Int, Long?> = HashMap()
                    for (k in vs.keys) {
                        v[k + dk] = vs[k]
                    }
                    next[n] = v
                } else {
                    val v = next[n]!!
                    for (k in vs.keys) {
                        if (!v.containsKey(k + dk)) {
                            v[k + dk] = vs[k]
                        } else {
                            v[k + dk] = v[k + dk]!! + vs[k]!!
                        }
                    }
                }
            }
        }
        return next
    }

    fun sum(counts: Map<String, MutableMap<Int, Long?>>, diff: Int, size: Int): Long {
        var result: Long = 0
        for (s in counts.keys) {
            val state = status[s]
            if (state!!.isOfTwoPieces) {
                for (k in counts[s]!!.keys) {
                    val k1 = size - k
                    if (abs(k - k1) <= diff) {
                        val d = counts[s]!![k]!!
                        result += d
                    }
                }
            }
        }
        return result
    }

    fun main(args: Array<String?>?) {
        val scanner = Scanner(System.`in`)
        val m = scanner.nextInt()
        val n = scanner.nextInt()
        val k = scanner.nextInt()
        if (m == 0 || n == 0) {
            println(1)
            return
        }
        setSize(n, m, k)
        val cs = Array(m) { IntArray(n) }
        for (i in 0 until m) {
            val s = scanner.next()
            for (j in 0 until n) {
                val ch = s[j]
                cs[i][j] = if (ch == 'T') T else if (ch == 'D') D else U
            }
        }
        constraint = cs
        solve()
    }
}
