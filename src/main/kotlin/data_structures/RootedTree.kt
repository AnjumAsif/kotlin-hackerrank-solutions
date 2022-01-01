package data_structures

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.PrintWriter
import java.util.*

var inputStream: InputStream? = null
var out: PrintWriter? = null
var INPUT = ""
var mod = 1000000007
fun solve() {
    val n = ni()
    val Q = ni()
    val root = ni() - 1
    val from = IntArray(n - 1)
    val to = IntArray(n - 1)
    for (i in 0 until n - 1) {
        from[i] = ni() - 1
        to[i] = ni() - 1
    }
    val graph = makeGraph(n, from, to)
    val data = makeParents(graph, root)
    val parents = data[0]
    val depths = data[2]
    val rights = makeRights(graph, parents, root)
    val iOrder = rights[1]
    val right = rights[2]
    val spar = logstepParents(parents)
    val f2 = LongArray(n + 2)
    val f1 = LongArray(n + 2)
    val f0 = LongArray(n + 2)
    val i2 = invert(2, mod.toLong())
    for (z in 0 until Q) {
        val t = nc()
        if (t == 'U') {
            val tar = ni() - 1
            val v = ni().toLong()
            val K = ni().toLong()
            val c = depths[tar].toLong()
            val c1 = (2 * v + (-2 * c + 1) * K) % mod
            val c0 = ((-c + 1) * v * 2 + -c * (-c + 1) % mod * K) % mod
            addFenwick(f2, iOrder[tar], K)
            addFenwick(f2, right[iOrder[tar]] + 1, -K)
            addFenwick(f1, iOrder[tar], c1)
            addFenwick(f1, right[iOrder[tar]] + 1, -c1)
            addFenwick(f0, iOrder[tar], c0)
            addFenwick(f0, right[iOrder[tar]] + 1, -c0)
        } else if (t == 'Q') {
            val a = ni() - 1
            val b = ni() - 1
            val lca = longestCommonAncestor(a, b, spar, depths)
            val plca = parents[lca]
            val vala = fenwickValue(a, f2, f1, f0, iOrder, depths)
            val valb = fenwickValue(b, f2, f1, f0, iOrder, depths)
            val vall = fenwickValue(lca, f2, f1, f0, iOrder, depths)
            var valpl = 0L
            if (plca != -1) {
                valpl = fenwickValue(plca, f2, f1, f0, iOrder, depths)
            }
            var result = (vala + valb - vall - valpl) * i2 % mod
            if (result < 0) {
                result += mod.toLong()
            }
            out!!.println(result)
        }
    }
}

fun invert(a: Long, mod: Long): Long {
    var a = a
    var b = mod
    var p: Long = 1
    var q: Long = 0
    while (b > 0) {
        val c = a / b
        var d: Long
        d = a
        a = b
        b = d % b
        d = p
        p = q
        q = d - c * q
    }
    return if (p < 0) {
        p + mod
    } else p
}

fun restoreFenwick(ft: LongArray): LongArray {
    val n = ft.size - 1
    val ret = LongArray(n)
    for (i in 0 until n) {
        ret[i] = sumFenwick(ft, i)
    }
    for (i in n - 1 downTo 1) {
        ret[i] -= ret[i - 1]
    }
    return ret
}

fun fenwickValue(a: Int, f2: LongArray, f1: LongArray, f0: LongArray, iord: IntArray, dep: IntArray): Long {
    val f2f = sumFenwick(f2, iord[a])
    val f1f = sumFenwick(f1, iord[a])
    val f0f = sumFenwick(f0, iord[a])
    return ((f2f % mod * dep[a] + f1f) % mod * dep[a] + f0f) % mod
}

fun sumFenwick(ft: LongArray, i: Int): Long {
    var i = i
    var sum: Long = 0
    i++
    while (i > 0) {
        sum += ft[i]
        i -= i and -i
    }
    return sum
}

fun addFenwick(ft: LongArray, i: Int, v: Long) {
    var i = i
    if (v == 0L) {
        return
    }
    val n = ft.size
    i++
    while (i < n) {
        ft[i] += v
        i += i and -i
    }
}

fun highestOneBit(i: Int): Int {
    if (i == 0) {
        return 0
    }
    var k = 1
    while (k < i) {
        k = k * 2
    }
    return k
}

fun numberOfTrailingZeros(t: Int): Int {
    val ZerosOnRightModLookup = intArrayOf(
        32,
        0,
        1,
        26,
        2,
        23,
        27,
        0,
        3,
        16,
        24,
        30,
        28,
        11,
        0,
        13,
        4,
        7,
        17,
        0,
        25,
        22,
        31,
        15,
        29,
        10,
        12,
        6,
        0,
        21,
        14,
        9,
        5,
        20,
        8,
        19,
        18
    )
    return if (t == 0) {
        1
    } else ZerosOnRightModLookup[(t and -t) % 37]
}

fun longestCommonAncestor(a: Int, b: Int, spar: Array<IntArray>, depth: IntArray): Int {
    var a = a
    var b = b
    if (depth[a] < depth[b]) {
        b = ancestor(b, depth[b] - depth[a], spar)
    } else if (depth[a] > depth[b]) {
        a = ancestor(a, depth[a] - depth[b], spar)
    }
    if (a == b) {
        return a
    }
    var sa = a
    var sb = b
    var low = 0
    var high = depth[a]
    var t = highestOneBit(high)
    var k = numberOfTrailingZeros(t)
    while (t > 0) {
        if (low xor high >= t) {
            if (spar[k][sa] != spar[k][sb]) {
                low = low or t
                sa = spar[k][sa]
                sb = spar[k][sb]
            } else {
                high = low or t - 1
            }
        }
        t = t ushr 1
        k--
    }
    return spar[0][sa]
}

fun ancestor(a: Int, m: Int, spar: Array<IntArray>): Int {
    var a = a
    var m = m
    var i = 0
    while (m > 0 && a != -1) {
        if (m and 1 == 1) {
            a = spar[i][a]
        }
        m = m ushr 1
        i++
    }
    return a
}

fun logstepParents(par: IntArray): Array<IntArray> {
    val n = par.size
    val m = numberOfTrailingZeros(highestOneBit(n - 1)) + 1
    val pars = Array(m) { IntArray(n) }
    pars[0] = par
    for (j in 1 until m) {
        for (i in 0 until n) {
            if (pars[j - 1][i] == -1) {
                pars[j][i] = -1
            } else {
                pars[j][i] = pars[j - 1][pars[j - 1][i]]
            }
        }
    }
    return pars
}

fun makeRights(g: Array<IntArray?>, par: IntArray, root: Int): Array<IntArray> {
    val n = g.size
    val ord = sortByPreorder(g, root)
    val iord = IntArray(n)
    for (i in 0 until n) {
        iord[ord[i]] = i
    }
    val right = IntArray(n)
    for (i in n - 1 downTo 0) {
        var v = i
        for (e in g[ord[i]]!!) {
            if (e != par[ord[i]]) {
                v = Math.max(v, right[iord[e]])
            }
        }
        right[i] = v
    }
    return arrayOf(ord, iord, right)
}

fun sortByPreorder(g: Array<IntArray?>, root: Int): IntArray {
    val n = g.size
    val stack = IntArray(n)
    val ord = IntArray(n)
    val ved = BitSet()
    stack[0] = root
    var p = 1
    var r = 0
    ved.set(root)
    while (p > 0) {
        val cur = stack[p - 1]
        ord[r] = cur
        r++
        p--
        for (e in g[cur]!!) {
            if (!ved[e]) {
                stack[p] = e
                p++
                ved.set(e)
            }
        }
    }
    return ord
}

fun makeParents(g: Array<IntArray?>, root: Int): Array<IntArray> {
    val n = g.size
    val par = IntArray(n)
    for (p in 0 until n) {
        par[p] = -1
    }
    val depth = IntArray(n)
    depth[0] = 0
    val q = IntArray(n)
    q[0] = root
    var p = 0
    var r = 1
    while (p < r) {
        val cur = q[p]
        for (nex in g[cur]!!) {
            if (par[cur] != nex) {
                q[r] = nex
                r++
                par[nex] = cur
                depth[nex] = depth[cur] + 1
            }
        }
        p++
    }
    return arrayOf(par, q, depth)
}

fun makeGraph(n: Int, from: IntArray, to: IntArray): Array<IntArray?> {
    val g = arrayOfNulls<IntArray>(n)
    val p = IntArray(n)
    for (f in from) {
        p[f]++
    }
    for (t in to) {
        p[t]++
    }
    for (i in 0 until n) {
        g[i] = IntArray(p[i])
    }
    for (i in from.indices) {
        p[from[i]]--
        g[from[i]]!![p[from[i]]] = to[i]
        p[to[i]]--
        g[to[i]]!![p[to[i]]] = from[i]
    }
    return g
}

val inbuf = ByteArray(1024)
var lenbuf = 0
var ptrbuf = 0

fun readByte(): Int {
    if (lenbuf == -1) throw InputMismatchException()
    if (ptrbuf >= lenbuf) {
        ptrbuf = 0
        try {
            lenbuf = inputStream!!.read(inbuf)
        } catch (e: IOException) {
            throw InputMismatchException()
        }
        if (lenbuf <= 0) return -1
    }
    return inbuf[ptrbuf++].toInt()
}

fun isSpaceChar(c: Int): Boolean {
    return !(c >= 33 && c <= 126)
}

fun skip(): Int {
    var b: Int
    while (readByte().also { b = it } != -1 && isSpaceChar(b));
    return b
}

fun nc(): Char {
    return skip().toChar()
}

fun ni(): Int {
    var num = 0
    var b: Int
    var minus = false
    while (readByte().also { b = it } != -1 && !(b >= '0'.toInt() && b <= '9'.toInt() || b == '-'.toInt()));
    if (b == '-'.toInt()) {
        minus = true
        b = readByte()
    }
    while (true) {
        num = if (b >= '0'.toInt() && b <= '9'.toInt()) {
            num * 10 + (b - '0'.toInt())
        } else {
            return if (minus) -num else num
        }
        b = readByte()
    }
}

fun main(args: Array<String>) {
    /* Enter your code here. Read input from STDIN. Print output to STDOUT. */
    inputStream = if (INPUT.isEmpty()) System.`in` else ByteArrayInputStream(INPUT.toByteArray())
    out = PrintWriter(System.out)
    solve()
    out!!.flush()
}
