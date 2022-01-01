package data_structures

import java.io.*
import java.util.*
import kotlin.math.sqrt

const val N = 100000
const val V = (N + 2) * 2
lateinit var a: Array<Vector?>
fun hypot(x: Double, y: Double): Double {
    return sqrt(x * x + y * y)
}

fun abs(p: Vector): Double {
    return hypot(p.x, p.y)
}

fun inCircle(c: Circle, a: Vector?): Boolean {
    return hypot(a!!.x - c.x, a.y - c.y) <= c.r
}

fun circumCenter(p: Vector?, q: Vector?, r: Vector?): Vector {
    val a = p!!.minus(r)
    val b = q!!.minus(r)
    val c = Vector(a.mult(p.add(r)) / 2, b.mult(q.add(r)) / 2)
    val x = Vector(a.x, b.x)
    val tmp = Vector(c.modulo(Vector(a.y, b.y)), x.modulo(c))
    return tmp.div(a.modulo(b))
}

fun smallestEnclosingCircle(n: Int): Circle {
    var circle = Circle(0.0, 0.0, -1.0)
    for (i in 0 until n) {
        if (!inCircle(circle, a[i])) {
            circle = Circle(a[i], 0.0)
            for (j in 0 until i) {
                if (!inCircle(circle, a[j])) {
                    circle = Circle(
                        a[i]!!.add(a[j]).div(2.0), abs(
                            a[i]!!.minus(a[j])
                        ) / 2
                    )
                    for (k in 0 until j) {
                        if (!inCircle(circle, a[k])) {
                            val o = circumCenter(a[i], a[j], a[k])
                            circle = Circle(o, abs(o.minus(a[k])))
                        }
                    }
                }
            }
        }
    }
    return circle
}

var y = IntArray(N)
var ny = 0
var pool = arrayOfNulls<Node>(V)
var allo = 0
var nodeNull = Node()
fun splay(x: Node?, k: Int): Node {
    var x = x
    var k = k
    var lspine: Node? = nodeNull
    var rspine: Node? = nodeNull
    while (true) {
        x!!.untag()
        if (x.l!!.size == k) {
            break
        }
        if (x.l!!.size < k) {
            k -= x.l!!.size + 1
            val y = x.r
            y!!.untag()
            if (x.r!!.cmp(k) == 1) {
                k -= x.r!!.l!!.size + 1
                x.r = y.l
                y.l = x
                x.mConcat()
                x = y.r
                y.r = lspine
                lspine = y
            } else {
                x.r = lspine
                lspine = x
                x = y
            }
        } else {
            val y = x.l
            y!!.untag()
            if (x.l!!.cmp(k) == 0) {
                x.l = y.r
                y.r = x
                x.mConcat()
                x = y.l
                y.l = rspine
                rspine = y
            } else {
                x.l = rspine
                rspine = x
                x = y
            }
        }
    }
    run {
        var z = x!!.l
        while (lspine !== nodeNull) {
            val y = lspine!!.r
            lspine!!.r = z
            lspine!!.mConcat()
            z = lspine
            lspine = y
        }
        x.l = z
    }
    run {
        var z = x!!.r
        while (rspine !== nodeNull) {
            val y = rspine!!.l
            rspine!!.l = z
            rspine!!.mConcat()
            z = rspine
            rspine = y
        }
        x.r = z
    }
    x!!.mConcat()
    return x
}

fun range(rt: Node?, L: Int, R: Int): Node {
    var result = rt
    result = splay(result, L - 1)
    result.r = splay(result.r, R - L)
    return result
}

fun inorder(rt: Node?, d: Int) {
    if (rt !== nodeNull) {
        rt!!.untag()
        inorder(rt.l, d + 1)
        y[ny++] = rt.key
        inorder(rt.r, d + 1)
    }
}

fun main(args: Array<String>) {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(FileWriter(System.getenv("OUTPUT_PATH")))
    var st = StringTokenizer(br.readLine())
    val n = st.nextToken().toInt()
    var m = st.nextToken().toInt()
    for (i in pool.indices) {
        pool[i] = Node()
    }
    a = arrayOfNulls(n)
    for (i in a.indices) {
        a[i] = Vector()
    }
    val rt = arrayOf(
        pool[allo++], pool[allo++]
    )
    nodeNull.init(0, 0)
    for (d in 0..1) {
        rt[d]!!.init(-1, 1)
        st = StringTokenizer(br.readLine())
        for (i in 0..n) {
            val node = pool[allo]
            if (i < n) {
                val item = st.nextToken().toInt()
                node!!.init(item, i + 2)
            } else {
                node!!.init(-1, i + 2)
            }
            node.l = rt[d]
            rt[d] = pool[allo++]
        }
    }
    while (m-- > 0) {
        st = StringTokenizer(br.readLine())
        val query = st.nextToken().toInt()
        var l0: Int
        var r0: Int
        var id: Int
        var p: Node?
        var q: Node?
        when (query) {
            1 -> {
                id = st.nextToken().toInt()
                l0 = st.nextToken().toInt()
                r0 = st.nextToken().toInt()
                rt[id] = range(rt[id], l0, r0 + 1)
                rt[id]!!.r!!.l!!.flip()
            }
            2 -> {
                id = st.nextToken().toInt()
                l0 = st.nextToken().toInt()
                r0 = st.nextToken().toInt()
                val l1 = st.nextToken().toInt()
                val r1 = st.nextToken().toInt()
                if (r0 + 1 == l1) {
                    rt[id] = range(rt[id], l0, r0 + 1)
                    rt[id]!!.r!!.l!!.flip()
                    rt[id] = range(rt[id], l1, r1 + 1)
                    rt[id]!!.r!!.l!!.flip()
                    rt[id] = range(rt[id], l0, r1 + 1)
                    rt[id]!!.r!!.l!!.flip()
                } else {
                    rt[id] = range(rt[id], l1, r1 + 1)
                    q = rt[id]!!.r!!.l
                    rt[id]!!.r!!.l = nodeNull
                    rt[id]!!.r!!.mConcat()
                    rt[id]!!.mConcat()
                    rt[id] = range(rt[id], l0, r0 + 1)
                    p = rt[id]!!.r!!.l
                    rt[id]!!.r!!.l = q
                    rt[id]!!.r!!.mConcat()
                    rt[id]!!.mConcat()
                    val s = l1 + (r1 - r0) - (l1 - l0)
                    rt[id] = range(rt[id], s, s)
                    rt[id]!!.r!!.l = p
                    rt[id]!!.r!!.mConcat()
                    rt[id]!!.mConcat()
                }
            }
            3 -> {
                l0 = st.nextToken().toInt()
                r0 = st.nextToken().toInt()
                rt[0] = range(rt[0], l0, r0 + 1)
                p = rt[0]!!.r!!.l
                rt[1] = range(rt[1], l0, r0 + 1)
                q = rt[1]!!.r!!.l
                rt[0]!!.r!!.l = q
                rt[0]!!.r!!.mConcat()
                rt[0]!!.mConcat()
                rt[1]!!.r!!.l = p
                rt[1]!!.r!!.mConcat()
                rt[1]!!.mConcat()
            }
            4 -> {
                l0 = st.nextToken().toInt()
                r0 = st.nextToken().toInt()
                ny = 0
                rt[0] = range(rt[0], l0, r0 + 1)
                p = rt[0]!!.r!!.l
                inorder(p, 0)
                id = ny
                run {
                    var i = 0
                    while (i < id) {
                        a[i]!!.x = y[i].toDouble()
                        i++
                    }
                }
                ny = 0
                rt[1] = range(rt[1], l0, r0 + 1)
                q = rt[1]!!.r!!.l
                inorder(q, 0)
                var i = 0
                while (i < id) {
                    a[i]!!.y = y[i].toDouble()
                    i++
                }
                val result = smallestEnclosingCircle(id).r
                bw.write(String.format("%.2f\n", result))
            }
            else -> {}
        }
    }
    bw.newLine()
    bw.close()
    br.close()
}

class Vector(var x: Double = 0.0, var y: Double = 0.0) {

    fun add(q: Vector?): Vector {
        return Vector(x + q!!.x, y + q.y)
    }

    operator fun minus(q: Vector?): Vector {
        return Vector(x - q!!.x, y - q.y)
    }

    operator fun div(t: Double): Vector {
        return Vector(x / t, y / t)
    }

    fun mult(q: Vector): Double {
        return x * q.x + y * q.y
    }

    fun modulo(q: Vector): Double {
        return x * q.y - y * q.x
    }
}

class Circle {
    var x: Double
    var y: Double
    var r: Double

    constructor(x: Double, y: Double, r: Double) {
        this.x = x
        this.y = y
        this.r = r
    }

    constructor(a: Vector?, r: Double) {
        x = a!!.x
        this.y = a.y
        this.r = r
    }
}

class Node {
    var flp = false
    var l: Node? = null
    var r: Node? = null
    var key = 0
    var size = 0
    fun init(k: Int, s: Int) {
        flp = false
        key = k
        size = s
        r = nodeNull
        l = r
    }

    fun cmp(k: Int): Int {
        if (l!!.size == k) {
            return -1
        }
        return if (l!!.size < k) 1 else 0
    }

    fun mConcat() {
        size = l!!.size + 1 + r!!.size
    }

    fun untag() {
        if (flp) {
            flp = false
            l!!.flip()
            r!!.flip()
        }
    }

    fun flip() {
        if (this === nodeNull) {
            return
        }
        val tmp = l
        l = r
        r = tmp
        flp = !flp
    }
}

