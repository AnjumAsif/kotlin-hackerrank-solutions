package data_structures

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.*


class Solution {

/*
 * Complete the 'solve' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts following parameters:
 *  1. INTEGER_ARRAY a
 *  2. 2D_INTEGER_ARRAY queries
 */

    fun size(x: Node?): Int {
        return x?.size ?: 0
    }

    fun sum(x: Node?): Long {
        return x?.sum ?: 0
    }

    fun split(x: Node?, left: Int): Array<Node?> {
        if (x == null) {
            return arrayOfNulls(2)
        }
        val p: Array<Node?>
        if (left <= size(x.l)) {
            p = split(x.l, left)
            x.l = p[1]
            p[1] = x
        } else {
            p = split(x.r, left - size(x.l) - 1)
            x.r = p[0]
            p[0] = x
        }
        x.rehash()
        return p
    }

    fun splitAt(x: Node?, vararg at: Int): Array<Node?> {
        var x = x
        val ret = arrayOfNulls<Node>(at.size + 1)
        for (i in at.indices.reversed()) {
            val tmp = split(x, at[i])
            ret[i + 1] = tmp[1]
            x = tmp[0]
        }
        ret[0] = x
        return ret
    }

    fun merge(l: Node?, r: Node?): Node? {
        if (l == null) {
            return r
        }
        if (r == null) {
            return l
        }
        return if (l.depth > r.depth) {
            r.l = merge(l, r.l)
            r.rehash()
            r
        } else {
            l.r = merge(l.r, r)
            l.rehash()
            l
        }
    }

    fun mergeAll(vararg nodes: Node?): Node? {
        var ret: Node? = null
        for (node in nodes) {
            ret = merge(ret, node)
        }
        return ret
    }

    fun solve(`in`: Input, out: PrintWriter) {
        val n = `in`.nextInt()
        val q = `in`.nextInt()
        var even: Node? = null
        var odd: Node? = null
        for (i in 0 until n) {
            if (i % 2 == 0) {
                even = merge(even, Node(`in`.nextLong()))
            } else {
                odd = merge(odd, Node(`in`.nextLong()))
            }
        }
        for (it in 0 until q) {
            val type = `in`.nextInt()
            val l = `in`.nextInt() - 1
            val r = `in`.nextInt()
            val splitEven = splitAt(even, (l + 1) / 2, (r + 1) / 2)
            val splitOdd = splitAt(odd, l / 2, r / 2)
            if (type == 1) {
                if (splitEven[1]!!.size != (r - l) / 2 || splitOdd[1]!!.size != (r - l) / 2) {
                    throw AssertionError()
                }
                even = mergeAll(splitEven[0], splitOdd[1], splitEven[2])
                odd = mergeAll(splitOdd[0], splitEven[1], splitOdd[2])
            } else {
                out.println(sum(splitEven[1]) + sum(splitOdd[1]))
                even = mergeAll(*splitEven)
                odd = mergeAll(*splitOdd)
            }
        }
    }

    fun main(args: Array<String>) {
        val out = PrintWriter(System.out)
        solve(Input(BufferedReader(InputStreamReader(System.`in`))), out)
        out.close()
    }

    class Node(node: Long) {

        companion object {
            val rnd = Random(42)
        }

        var l: Node? = null
        var r: Node? = null
        var depth: Int
        var size = 0
        var `val`: Long
        var sum: Long = 0

        init {
            depth = rnd.nextInt()
            this.`val` = node
            rehash()
        }

        fun rehash() {
            size = 1
            sum = `val`
            if (l != null) {
                size += l!!.size
                sum += l!!.sum
            }
            if (r != null) {
                size += r!!.size
                sum += r!!.sum
            }
        }

    }

    class Input(var bufferedReader: BufferedReader) {
        var sb = StringBuilder()

        operator fun next(): String? {
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

        fun nextLong(): Long {
            return next()!!.toLong()
        }

    }

}