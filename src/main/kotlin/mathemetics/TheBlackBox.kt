package mathemetics

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.*

class TheBlackBox {

    class Segment(var l: Int, var r: Int, var number: Int)

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

    fun solve(input: Input, out: PrintWriter) {
        val n = input.nextInt()
        val a = IntArray(n)
        val segments = ArrayList<Segment>()
        val prev = HashMap<Int, Int>()
        for (i in 0 until n) {
            a[i] = input.nextInt()
            if (a[i] > 0) {
                prev[a[i]] = i
            } else {
                segments.add(Segment(prev[-a[i]]!!, i, -a[i]))
                prev.remove(-a[i])
            }
        }
        for (element in prev) {
            segments.add(Segment(element.value, n, element.key))
        }
        val basis = IntArray(30)
        Arrays.fill(basis, -1)
        solve(0, n, basis, segments, out)
    }

    private fun solve(l: Int, r: Int, basis: IntArray, segments: ArrayList<Segment>, out: PrintWriter) {
        var maskAt = 0
        for (s in segments) {
            if (s.l <= l && r <= s.r) {
                maskAt = maskAt or updateBasis(basis, s.number)
            }
        }
        if (l + 1 == r) {
            out.println(max(basis))
        } else {
            val mid = (l + r) / 2
            val lSeg = ArrayList<Segment>()
            val rSeg = ArrayList<Segment>()
            for (s in segments) {
                if (s.l > l || r > s.r) {
                    if (s.l < mid) {
                        lSeg.add(s)
                    }
                    if (mid < s.r) {
                        rSeg.add(s)
                    }
                }
            }
            solve(l, mid, basis, lSeg, out)
            solve(mid, r, basis, rSeg, out)
        }
        while (maskAt != 0) {
            val at = Integer.numberOfTrailingZeros(maskAt)
            maskAt = maskAt xor (1 shl at)
            basis[at] = -1
        }
    }

    private fun updateBasis(basis: IntArray, number: Int): Int {
        var x = number
        for (i in basis.indices.reversed()) {
            if (basis[i] != -1 && x and (1 shl i) != 0) {
                x = x xor basis[i]
            }
        }
        if (x != 0) {
            val at = 31 - Integer.numberOfLeadingZeros(x)
            basis[at] = x
            return 1 shl at
        }
        return 0
    }

    private fun max(basis: IntArray): Int {
        var x = 0
        for (i in basis.indices.reversed()) {
            if (basis[i] != -1 && x and (1 shl i) == 0) {
                x = x xor basis[i]
            }
        }
        return x
    }

    fun main(args: Array<String>) {
        val out = PrintWriter(System.out)
        solve(Input(BufferedReader(InputStreamReader(System.`in`))), out)
        out.close()
    }
}