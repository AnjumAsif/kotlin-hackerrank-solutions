package mathemetics

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

class Teleporters {
    fun size(n: Node?): Int {
        return n?.size ?: 0
    }

    fun fixUp(n: Node?) {
        n!!.size = 1 + size(n.l) + size(n.r)
    }

    fun fixDown(n: Node?) {
        if (n!!.reversed) {
            val tmp = n.l
            n.l = n.r
            n.r = tmp
            if (n.l != null) {
                n.l!!.reversed = n.l!!.reversed xor true
            }
            if (n.r != null) {
                n.r!!.reversed = n.r!!.reversed xor true
            }
            n.reversed = false
        }
    }

    fun rotate(x: Node?) {
        val y = x!!.p
        x.p = y!!.p
        if (y.root) {
            y.root = false
            x.root = true
        } else if (y === y.p!!.l) {
            y.p!!.l = x
        } else {
            y.p!!.r = x
        }
        y.p = x
        if (x === y.l) {
            y.l = x.r
            if (y.l != null) {
                y.l!!.p = y
            }
            x.r = y
        } else {
            y.r = x.l
            if (y.r != null) {
                y.r!!.p = y
            }
            x.l = y
        }
        fixUp(y)
        fixUp(x)
    }

    fun splay(x: Node?) {
        while (!x!!.root) {
            if (!x.p!!.root) {
                fixDown(x.p!!.p)
            }
            fixDown(x.p)
            fixDown(x)
            if (x.p!!.root) {
                rotate(x)
            } else {
                if (x === x.p!!.l && x.p === x.p!!.p!!.l || x === x.p!!.r && x.p === x.p!!.p!!.r) {
                    rotate(x.p)
                    rotate(x)
                } else {
                    rotate(x)
                    rotate(x)
                }
            }
        }
    }

    fun join(l: Node?, x: Node?, r: Node?): Node {
        x!!.reversed = false
        x.l = l
        x.r = r
        if (l != null) {
            l.p = x
            l.root = false
        }
        if (r != null) {
            r.p = x
            r.root = false
        }
        fixUp(x)
        return x
    }

    var q: Node? = null
    var r: Node? = null
    fun split(x: Node) {
        splay(x)
        fixDown(x)
        if (x.l != null) {
            x.l!!.root = true
            x.l!!.p = null
        }
        if (x.r != null) {
            x.r!!.root = true
            x.r!!.p = null
        }
        x.p = null
        q = x.l
        r = x.r
        x.r = null
        x.l = x.r
        fixUp(x)
    }

    fun findPath(x: Node): Node {
        splay(x)
        return x
    }

    fun link(x: Node?, y: Node?) {
        join(null, expose(x), expose(y)).p = null
    }

    fun unlink(x: Node?) {
        expose(x)
        splay(x)
        fixDown(x)
        if (x!!.r != null) {
            x.r!!.p = null
            x.r!!.root = true
        }
        x.r = null
        fixUp(x)
    }

    fun expose(v: Node?): Node {
        var v = v
        var p: Node? = null
        while (v != null) {
            val w = findPath(v).p
            split(v)
            if (q != null) {
                q!!.p = v
            }
            p = join(p, v, r)
            v = w
        }
        p!!.p = null
        return p
    }

    fun getp(i: Int, x: Int, ps: Array<IntArray>): Int {
        var i = i
        var x = x
        while (x > 0) {
            val l = 31 - Integer.numberOfLeadingZeros(x)
            x -= 1 shl l
            i = ps[l][i]
        }
        return i
    }

    fun solve(input: Input, out: PrintWriter) {
        val n = input.nextInt()
        val qs = input.nextInt()
        val a = IntArray(n)
        for (i in 0 until n) {
            a[i] = input.nextInt()
        }
        val edges: Array<ArrayList<Int>?> = arrayOfNulls<ArrayList<Int>>(n)
        for (i in 0 until n) {
            edges[i] = ArrayList()
        }
        for (i in 0 until n - 1) {
            val x = input.nextInt() - 1
            val y = input.nextInt() - 1
            edges[x]!!.add(y)
            edges[y]!!.add(x)
        }
        val ps = Array(17) { IntArray(n) }
        dfs(0, -1, edges, ps[0])
        ps[0][0] = 0
        for (l in 1 until ps.size) {
            for (i in 0 until n) {
                ps[l][i] = ps[l - 1][ps[l - 1][i]]
            }
        }
        val ns = arrayOfNulls<Node>(n)
        for (i in 0 until n) {
            ns[i] = Node(i)
        }
        for (i in 1 until n) {
            link(ns[i], ns[getp(i, a[i], ps)])
        }
        for (it in 0 until qs) {
            if (input.nextInt() == 1) {
                val i = input.nextInt() - 1
                val newa = input.nextInt()
                if (i != 0) {
                    unlink(ns[i])
                    link(ns[i], ns[getp(i, newa, ps)])
                }
            } else {
                val i = input.nextInt() - 1
                out.println(expose(ns[i])!!.size - 1)
            }
        }
    }

    fun dfs(i: Int, p: Int, edges: Array<ArrayList<Int>?>, ps: IntArray) {
        ps[i] = p
        for (j in edges[i]!!) {
            if (j != p) {
                dfs(j, i, edges, ps)
            }
        }
    }

    class Node(var ind: Int) {
        var l: Node? = null
        var r: Node? = null
        var p: Node? = null
        var reversed = false
        var root = true
        var size = 1
        override fun toString(): String {
            var ans = "$ind: ["
            ans += "size = $size"
            ans += ", p = " + if (p == null) "null" else p!!.ind
            ans += ", l = " + if (l == null) "null" else l!!.ind
            ans += ", r = " + if (r == null) "null" else r!!.ind
            if (root) {
                ans += ", root"
            }
            if (reversed) {
                ans += ", reversed"
            }
            ans = "$ans]"
            return ans
        }
    }

    class Input(var bufferedReader: BufferedReader) {
        var sb = StringBuilder()
        fun next(): String? {
            sb.setLength(0)
            while (true) {
                val c = bufferedReader.read()
                if (c == -1) {
                    return null
                }
                if (" \n\r\t".indexOf(c.toChar()) == -1) {
                    sb.append(c.toChar())
                    break
                }
            }
            while (true) {
                val c = bufferedReader.read()
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

    }

    fun main(args: Array<String>) {
        val out = PrintWriter(System.out)
        solve(Input(BufferedReader(InputStreamReader(System.`in`))), out)
        out.close()
    }
}