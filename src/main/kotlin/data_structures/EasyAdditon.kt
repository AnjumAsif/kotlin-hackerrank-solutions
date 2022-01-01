package data_structures

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.InputStreamReader
import java.util.*

lateinit var next: IntArray
lateinit var successor: IntArray
lateinit var ptr: IntArray
var index = 1
fun addEdge(u: Int, v: Int) {
    next[index] = ptr[u]
    ptr[u] = index
    successor[index++] = v
}

fun lowestOneBit(v: Int): Int {
    return v and v.inv() + 1
}

fun highestOneBitMask(v: Int): Int {
    var v = v
    v = v or (v shr 1)
    v = v or (v shr 2)
    v = v or (v shr 4)
    v = v or (v shr 8)
    v = v or (v shr 16)
    return v shr 1
}

lateinit var tParent: IntArray
var tOrd: MutableList<Int> = ArrayList()
fun treeGetOrder(g: Array<MutableList<Int>?>, root: Int) {
    val n = g.size
    tParent = IntArray(n)
    Arrays.fill(tParent, -1)
    tOrd.clear()
    val stk: Deque<Int> = LinkedList()
    stk.add(root)
    while (!stk.isEmpty()) {
        val i = stk.remove()
        tOrd.add(i)
        for (j in g[i]!!.indices.reversed()) {
            val c = g[i]!![j]
            if (tParent[c] == -1 && c != root) {
                stk.add(c)
            } else {
                tParent[i] = c
            }
        }
    }
}

const val MOD: Long = 1000000007
fun sum(a: Long, b: Long): Long {
    return (a + b) % MOD
}

fun multiply(a: Long, b: Long): Long {
    return a * b % MOD
}

fun multiply(a: Long, b: Long, c: Long): Long {
    return a * (b * c % MOD) % MOD
}

fun multiply(a: Long, b: Long, c: Long, d: Long): Long {
    return multiply(multiply(a, b), multiply(c, d))
}

fun querySub(adds: IntArray, a: Int, b: Int, x: Long) {
    var x = x
    if (x < 0) {
        x += MOD
    }
    adds[a] = sum(adds[a].toLong(), x).toInt()
    adds[b] = sum(adds[b].toLong(), MOD - x).toInt()
}

fun inverse(x: Int): Long {
    var a = x.toLong()
    var b = MOD
    var u: Long = 1
    var v: Long = 0
    while (b > 0) {
        val t = a / b
        a -= t * b
        var temp = a
        a = b
        b = temp
        u = sum(u, MOD - multiply(t, v))
        temp = u
        u = v
        v = temp
    }
    return u
}

fun getPowRs(n: Int, r: Int): IntArray {
    val powRs = IntArray(n * 2 + 1)
    powRs[n] = 1
    for (i in 1..n) {
        powRs[n + i] = multiply(powRs[n + i - 1].toLong(), r.toLong()).toInt()
    }
    val invR = inverse(r)
    for (i in 1..n) {
        powRs[n - i] = multiply(powRs[n - i + 1].toLong(), invR).toInt()
    }
    return powRs
}

fun getCoefficients(n: Int, powRs: IntArray, depth: IntArray): Array<IntArray> {
    val coeficients = Array(T) { IntArray(n + 1) }
    for (i in 0 until n) {
        val d = depth[i]
        coeficients[0][i] = powRs[n - d]
        coeficients[1][i] = powRs[n + d]
        coeficients[2][i] = multiply(powRs[n - d].toLong(), MOD - d).toInt()
        coeficients[3][i] = multiply(powRs[n + d].toLong(), +d.toLong()).toInt()
        coeficients[4][i] = multiply(powRs[n - d].toLong(), MOD - d, MOD - d).toInt()
        coeficients[5][i] = multiply(powRs[n + d].toLong(), +d.toLong(), +d.toLong()).toInt()
    }
    return coeficients
}

const val T = 6

class SchieberVishkinLCA {
    lateinit var indices: IntArray
    private lateinit var maxHIndices: IntArray
    private lateinit var ancestorHeights: IntArray
    private lateinit var pathParents: IntArray
    fun build(n: Int) {
        ancestorHeights = IntArray(n)
        val parents = IntArray(n)
        maxHIndices = IntArray(n)
        val vertices: Deque<Int> = LinkedList()
        indices = IntArray(n)
        var currentIndex = 1
        vertices.add(0)
        while (!vertices.isEmpty()) {
            val v = vertices.removeLast()
            indices[v] = currentIndex++
            var it = ptr[v]
            while (it > 0) {
                val u = successor[it]
                parents[u] = v
                vertices.add(u)
                it = next[it]
            }
        }
        var head = 0
        var tail = 1
        val vertices2 = IntArray(n)
        while (head != tail) {
            val v = vertices2[head++]
            var it = ptr[v]
            while (it > 0) {
                val u = successor[it]
                vertices2[tail++] = u
                it = next[it]
            }
        }
        for (i in 0 until tail) {
            val it = vertices2[i]
            maxHIndices[it] = indices[it]
        }
        for (i in tail - 1 downTo 0) {
            val it = vertices2[i]
            val parent = parents[it]
            if (lowestOneBit(maxHIndices[parent]) < lowestOneBit(maxHIndices[it])) {
                maxHIndices[parent] = maxHIndices[it]
            }
        }
        ancestorHeights[0] = 0
        for (i in 0 until tail) {
            val it = vertices2[i]
            ancestorHeights[it] = ancestorHeights[parents[it]] or lowestOneBit(maxHIndices[it])
        }
        pathParents = parents
        pathParents[indices[0] - 1] = 0
        for (i in 0 until tail) {
            val it = vertices2[i]
            var jt = ptr[it]
            while (jt > 0) {
                val u = successor[jt]
                if (maxHIndices[it] != maxHIndices[u]) {
                    pathParents[indices[u] - 1] = it
                } else {
                    pathParents[indices[u] - 1] = pathParents[indices[it] - 1]
                }
                jt = next[jt]
            }
        }
    }

    fun query(v: Int, u: Int): Int {
        val iV = maxHIndices[v]
        val iU = maxHIndices[u]
        val hIv = lowestOneBit(iV)
        val hIu = lowestOneBit(iU)
        val hbMask = highestOneBitMask(iV xor iU or hIv or hIu)
        val j = lowestOneBit(ancestorHeights[v] and ancestorHeights[u] and hbMask.inv())
        val x: Int = if (j == hIv) {
            v
        } else {
            val kMask = highestOneBitMask(ancestorHeights[v] and j - 1)
            pathParents[(indices[v] and kMask.inv() or kMask + 1) - 1]
        }
        val y: Int = if (j == hIu) {
            u
        } else {
            val kMask = highestOneBitMask(ancestorHeights[u] and j - 1)
            pathParents[(indices[u] and kMask.inv() or kMask + 1) - 1]
        }
        return if (indices[x] < indices[y]) x else y
    }
}

fun main(args: Array<String>) {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(FileWriter(System.getenv("OUTPUT_PATH")))
    var st = StringTokenizer(br.readLine())

    val n = st.nextToken().toInt()
    val r = st.nextToken().toInt()

    val g: Array<MutableList<Int>?> = arrayOfNulls(n)

    for (i in 0 until n) {
        g[i] = ArrayList()
    }
    for (i in 0 until n - 1) {
        st = StringTokenizer(br.readLine())
        val x = st.nextToken().toInt() - 1
        val y = st.nextToken().toInt() - 1
        g[x]!!.add(y)
        g[y]!!.add(x)
    }
    treeGetOrder(g, 0)
    val depth = IntArray(n)
    for (j in 1 until n) {
        val i = tOrd[j]
        depth[i] = depth[tParent[i]] + 1
    }
    next = IntArray(n)
    successor = IntArray(n)
    ptr = IntArray(n)
    for (i in 1 until n) {
        addEdge(tParent[i], i)
    }
    val lca = SchieberVishkinLCA()
    lca.build(n)
    val powRs = getPowRs(n, r)
    val adds = Array(T) { IntArray(n + 1) }
    st = StringTokenizer(br.readLine())
    val u = st.nextToken().toInt()
    val q = st.nextToken().toInt()
    for (i in 0 until u) {
        st = StringTokenizer(br.readLine())
        val a1 = st.nextToken().toInt()
        val d1 = st.nextToken().toInt()
        val a2 = st.nextToken().toInt()
        val d2 = st.nextToken().toInt()
        val a = st.nextToken().toInt() - 1
        val b = st.nextToken().toInt() - 1
        val c = lca.query(a, b)
        val cp = if (c == 0) n else tParent[c]
        val dA = depth[a]
        val dB = depth[b]
        val dC = depth[c]
        val p0 = powRs[n + dA]
        val uB = dA + dB - dC * 2
        val q0 = powRs[n - dB + uB]
        var t = multiply(a1.toLong(), a2.toLong()).toInt()
        querySub(adds[0], a, cp, multiply(t.toLong(), p0.toLong()))
        querySub(adds[1], b, c, multiply(t.toLong(), q0.toLong()))
        t = sum(multiply(a1.toLong(), d2.toLong()), multiply(d1.toLong(), a2.toLong())).toInt()
        val e = -dB + uB
        querySub(adds[2], a, cp, multiply(t.toLong(), p0.toLong()))
        querySub(adds[0], a, cp, multiply(t.toLong(), p0.toLong(), dA.toLong()))
        querySub(adds[3], b, c, multiply(t.toLong(), q0.toLong()))
        querySub(adds[1], b, c, multiply(t.toLong(), q0.toLong(), e.toLong()))
        t = multiply(d1.toLong(), d2.toLong()).toInt()
        querySub(adds[4], a, cp, multiply(t.toLong(), p0.toLong()))
        querySub(adds[2], a, cp, multiply(t.toLong(), p0.toLong(), dA.toLong(), 2))
        querySub(adds[0], a, cp, multiply(t.toLong(), p0.toLong(), dA.toLong(), dA.toLong()))
        querySub(adds[5], b, c, multiply(t.toLong(), q0.toLong()))
        querySub(adds[3], b, c, multiply(t.toLong(), q0.toLong(), e.toLong(), 2))
        querySub(adds[1], b, c, multiply(t.toLong(), q0.toLong(), e.toLong(), e.toLong()))
    }
    val coefficients = getCoefficients(n, powRs, depth)
    val values = IntArray(n)
    for (t in 0 until T) {
        for (ix in n - 1 downTo 1) {
            val i = tOrd[ix]
            adds[t][tParent[i]] = sum(adds[t][tParent[i]].toLong(), adds[t][i].toLong()).toInt()
        }
        for (i in 0 until n) {
            adds[t][i] = multiply(adds[t][i].toLong(), coefficients[t][i].toLong()).toInt()
        }
        for (i in 0 until n) {
            values[i] = sum(values[i].toLong(), adds[t][i].toLong()).toInt()
        }
    }
    val sums = values.clone()
    for (ix in 1 until n) {
        val i = tOrd[ix]
        sums[i] = sum(sums[i].toLong(), sums[tParent[i]].toLong()).toInt()
    }
    for (ii in 0 until q) {
        st = StringTokenizer(br.readLine())
        val a = st.nextToken().toInt() - 1
        val b = st.nextToken().toInt() - 1
        val c = lca.query(a, b)
        var result: Long = 0
        result = sum(result, sums[a].toLong())
        result = sum(result, sums[b].toLong())
        result = sum(result, MOD - values[c])
        if (c != 0) {
            result = sum(result, MOD - multiply(sums[tParent[c]].toLong(), 2))
        }
        bw.write(
            """
                    $result
                    
                    """.trimIndent()
        )
    }
    bw.newLine()
    bw.close()
    br.close()
}
