package data_structures

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.util.*
import kotlin.experimental.and


fun main(args: Array<String>) {
    val inputStream = System.`in`
    val outputStream: OutputStream = System.out
    val inputReader = InputReader(inputStream)
    val out = PrintWriter(outputStream)
    val solver = NetworkAdministration()
    solver.solve(inputReader, out)
    out.close()
}

class NetworkAdministration {
    fun solve(inputReader: InputReader, out: PrintWriter) {
        val S = inputReader.nextInt()
        val L = inputReader.nextInt()
        val A = inputReader.nextInt()
        val T = inputReader.nextInt()
        val lct = Array(A) {
            arrayOfNulls<LinkCutTree.Node>(
                S
            )
        }
        val deg = Array(A) { ByteArray(S) }
        val infos = arrayOfNulls<Info>(L)
        for (i in 0 until L) {
            val x = inputReader.nextInt() - 1
            val y = inputReader.nextInt() - 1
            val a = inputReader.nextInt() - 1
            val edgeNode = LinkCutTree.Node(0)
            if (lct[a][x] == null) lct[a][x] = LinkCutTree.Node(0)
            if (lct[a][y] == null) lct[a][y] = LinkCutTree.Node(0)
            LinkCutTree.link(lct[a][x], edgeNode)
            LinkCutTree.link(lct[a][y], edgeNode)
            ++deg[a][x]
            ++deg[a][y]
            infos[i] = Info(a, edgeNode, edge(x, y))
        }
        Arrays.sort(
            infos
        ) { a, b -> a!!.edge.compareTo(b!!.edge) }
        val edges = LongArray(L)
        for (i in 0 until L) {
            edges[i] = infos[i]!!.edge
        }
        for (i in 0 until T) {
            val t = inputReader.nextInt()
            val a = inputReader.nextInt() - 1
            val b = inputReader.nextInt() - 1
            if (t == 1) {
                val admin = inputReader.nextInt() - 1
                val e = edge(a, b)
                val index = Arrays.binarySearch(edges, e)
                val info = if (index < 0) null else infos[index]
                val prevAdmin = info?.admin
                if (prevAdmin == null) {
                    out.println("Wrong link")
                } else if (prevAdmin == admin) {
                    out.println("Already controlled link")
                } else if (deg[admin][a].toInt() == 2 || deg[admin][b].toInt() == 2) {
                    out.println("Server overload")
                } else if (lct[admin][a] != null && lct[admin][b] != null && LinkCutTree.connected(
                        lct[admin][a], lct[admin][b]
                    )
                ) {
                    out.println("Network redundancy")
                } else {
                    out.println("Assignment done")
                    --deg[prevAdmin][a]
                    --deg[prevAdmin][b]
                    ++deg[admin][a]
                    ++deg[admin][b]
                    val edgeNode = info.edgeNode
                    LinkCutTree.cut(lct[prevAdmin][a], edgeNode)
                    LinkCutTree.cut(lct[prevAdmin][b], edgeNode)
                    if (lct[admin][a] == null) lct[admin][a] = LinkCutTree.Node(0)
                    if (lct[admin][b] == null) lct[admin][b] = LinkCutTree.Node(0)
                    LinkCutTree.link(lct[admin][a], edgeNode)
                    LinkCutTree.link(lct[admin][b], edgeNode)
                    info.admin = admin
                }
            } else if (t == 2) {
                val x = inputReader.nextInt()
                val edgeNode = infos[Arrays.binarySearch(edges, edge(a, b))]!!.edgeNode
                LinkCutTree.modify(edgeNode, edgeNode, x)
            } else {
                val admin = inputReader.nextInt() - 1
                if (lct[admin][a] == null || lct[admin][b] == null || !LinkCutTree.connected(
                        lct[admin][a], lct[admin][b]
                    )
                ) {
                    out.println("No connection")
                } else {
                    val res = LinkCutTree.query(lct[admin][a], lct[admin][b])
                    out.println("$res security devices placed")
                }
            }
        }
    }

    class Info(var admin: Int, var edgeNode: LinkCutTree.Node, var edge: Long)
    object LinkCutTree {
        fun queryOperation(leftValue: Int, rightValue: Int): Int {
            return leftValue + rightValue
        }

        val neutralValue: Int
            get() = 0

        fun getSize(root: Node?): Int {
            return root?.size ?: 0
        }

        fun getSubTreeValue(root: Node?): Int {
            return root?.subTreeValue ?: neutralValue
        }

        private fun connect(ch: Node?, p: Node?, isLeftChild: Boolean?) {
            if (ch != null) ch.parent = p
            if (isLeftChild != null) {
                if (isLeftChild) p!!.left = ch else p!!.right = ch
            }
        }

        private fun rotate(x: Node?) {
            val p = x!!.parent
            val g = p!!.parent
            val isRootP = p.isRoot
            val leftChildX = x === p.left
            connect(if (leftChildX) x.right else x.left, p, leftChildX)
            connect(p, x, !leftChildX)
            connect(x, g, if (isRootP) null else p === g!!.left)
            p.update()
        }

        private fun splay(x: Node?) {
            while (!x!!.isRoot) {
                val p = x.parent
                val g = p!!.parent
                if (!p.isRoot) g!!.push()
                p.push()
                x.push()
                if (!p.isRoot) rotate(if (x === p.left == (p === g!!.left)) p /*zig-zig*/ else x /*zig-zag*/)
                rotate(x)
            }
            x.push()
            x.update()
        }

        private fun expose(x: Node?): Node? {
            var last: Node? = null
            var y = x
            while (y != null) {
                splay(y)
                y.left = last
                last = y
                y = y.parent
            }
            splay(x)
            return last
        }

        private fun makeRoot(x: Node?) {
            expose(x)
            x!!.revert = !x.revert
        }

        fun connected(x: Node?, y: Node?): Boolean {
            if (x === y) return true
            expose(x)
            expose(y)
            return x!!.parent != null
        }

        fun link(x: Node?, y: Node?) {
            makeRoot(x)
            x!!.parent = y
        }

        fun cut(x: Node?, y: Node) {
            makeRoot(x)
            expose(y)
            y.right!!.parent = null
            y.right = null
        }

        fun query(from: Node?, to: Node?): Int {
            makeRoot(from)
            expose(to)
            return getSubTreeValue(to)
        }

        fun modify(from: Node?, to: Node, delta: Int) {
            makeRoot(from)
            expose(to)
            to.nodeValue = delta
        }

        class Node(var nodeValue: Int) {
            var subTreeValue: Int
            var size: Int
            var revert = false
            var left: Node? = null
            var right: Node? = null
            var parent: Node? = null

            init {
                subTreeValue = nodeValue
                size = 1
            }

            val isRoot: Boolean
                get() = parent == null || parent!!.left !== this && parent!!.right !== this

            fun push() {
                if (revert) {
                    revert = false
                    val t = left
                    left = right
                    right = t
                    if (left != null) left!!.revert = !left!!.revert
                    if (right != null) right!!.revert = !right!!.revert
                }
            }

            fun update() {
                subTreeValue = queryOperation(queryOperation(getSubTreeValue(left), nodeValue), getSubTreeValue(right))
                size = 1 + getSize(left) + getSize(right)
            }
        }
    }


    private fun edge(u: Int, v: Int): Long {
        return (u.coerceAtMost(v).toLong() shl 32) + u.coerceAtLeast(v)
    }

}

class InputReader(val inputStream: InputStream) {
    val buf = ByteArray(1024)
    var pos = 0
    var size = 0
    fun nextInt(): Int {
        var c = read()
        while (isWhitespace(c)) c = read()
        var sign = 1
        if (c == '-'.toInt()) {
            sign = -1
            c = read()
        }
        var res = 0
        do {
            if (c < '0'.toInt() || c > '9'.toInt()) throw InputMismatchException()
            res = res * 10 + c - '0'.toInt()
            c = read()
        } while (!isWhitespace(c))
        return res * sign
    }

    private fun read(): Int {
        if (size == -1) throw InputMismatchException()
        if (pos >= size) {
            pos = 0
            size = try {
                inputStream.read(buf)
            } catch (e: IOException) {
                throw InputMismatchException()
            }
            if (size <= 0) return -1
        }
        return (buf[pos++] and 255.toByte()).toInt()
    }

    private fun isWhitespace(c: Int): Boolean {
        return c == ' '.toInt() || c == '\n'.toInt() || c == '\r'.toInt() || c == '\t'.toInt() || c == -1
    }
}