package mathemetics

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.*


class Randomness {

    fun main(args: Array<String>) {
        val out = PrintWriter(System.out)
        solve(Input(BufferedReader(InputStreamReader(System.`in`))), out)
        out.close()
    }

    fun solve(input: Input, out: PrintWriter) {
        Random(42)
        val span = 8
        val n: Int = input.nextInt()
        val m: Int = input.nextInt()
        val cs: CharArray = input.next()!!.toCharArray()
        val map = HashMap<Long, Int>()
        var ans = 1L * n * (n + 1) / 2
        var len = 1
        while (len <= span && len <= n) {
            ans -= (n - len + 1).toLong()
            ++len
        }
        for (i in 0 until n) {
            var s: Long = 1
            var length = 1
            while (length <= span && i + length <= n) {
                s = 26 * s + cs[i + length - 1].toInt().toLong() - 'a'.toInt().toLong()
                if (map.containsKey(s)) {
                    map[s] = map[s]!! + 1
                } else {
                    map[s] = 1
                    ans++
                }
                ++length
            }
        }
        for (it in 0 until m) {
            val at: Int = input.nextInt() - 1
            val c: Char = input.next()!![0]
            for (i in 0.coerceAtLeast(at - span + 1)..at) {
                var s: Long = 1
                var length = 1
                while (length <= span && i + length <= n) {
                    s = 26 * s + cs[i + length - 1].toInt().toLong() - 'a'.toInt().toLong()
                    if (i + length <= at) {
                        ++length
                        continue
                    }
                    val value = map[s]!!
                    if (value > 1) {
                        map[s] = value - 1
                    } else {
                        map.remove(s)
                        ans--
                    }
                    ++length
                }
            }
            cs[at] = c
            for (i in 0.coerceAtLeast(at - span + 1)..at) {
                var s: Long = 1
                var length = 1
                while (length <= span && i + length <= n) {
                    s = 26 * s + cs[i + length - 1].toInt().toLong() - 'a'.toInt().toLong()
                    if (i + length <= at) {
                        ++length
                        continue
                    }
                    if (map.containsKey(s)) {
                        map[s] = map[s]!! + 1
                    } else {
                        map[s] = 1
                        ans++
                    }
                    ++length
                }
            }
            out.println(ans)
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
}