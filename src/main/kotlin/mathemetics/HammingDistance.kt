package mathemetics

import java.util.*

class Solution {
    private var outputStrBuffer: StringBuffer? = null
    fun main(args: Array<String>) {
        val scanner = Scanner(System.`in`)
        outputStrBuffer = StringBuffer()
        val n = scanner.nextInt()
        val chars = CharArray(n)
        val s = scanner.next()
        for (i in 0 until n) chars[i] = s[i]
        var m = scanner.nextInt()
        while (m-- > 0) {
            val exec = scanner.next()
            if (exec == null) {
                println("Null!")
            } else when (exec) {
                "C" -> change(chars, scanner.nextInt() - 1, scanner.nextInt() - 1, scanner.next()[0])
                "S" -> swap(
                    chars,
                    scanner.nextInt() - 1,
                    scanner.nextInt() - 1,
                    scanner.nextInt() - 1,
                    scanner.nextInt() - 1
                )
                "R" -> reverse(chars, scanner.nextInt() - 1, scanner.nextInt() - 1)
                "W" -> write(chars, scanner.nextInt() - 1, scanner.nextInt() - 1)
                else -> hamilton(chars, scanner.nextInt() - 1, scanner.nextInt() - 1, scanner.nextInt())
            }
        }
        println(outputStrBuffer.toString())
    }

    private fun swap(chars: CharArray, l1: Int, r1: Int, l2: Int, r2: Int) {
        val tmp: CharArray = chars.copyOfRange(l1, r2 + 1)
        var p = l1
        for (i in 0 until r2 - l2 + 1) {
            chars[p] = tmp[l2 + i - l1]
            p++
        }
        for (i in 0 until l2 - r1 - 1) {
            chars[p] = tmp[r1 + 1 + i - l1]
            p++
        }
        for (i in 0 until r1 - l1 + 1) {
            chars[p] = tmp[l1 + i - l1]
            p++
        }
    }

    private fun change(chars: CharArray, l: Int, r: Int, c: Char) {
        for (i in l..r) {
            chars[i] = c
        }
    }

    private fun reverse(chars: CharArray, l: Int, r: Int) {
        var lp = l
        var rp = r
        while (lp < rp) {
            val tmp = chars[lp]
            chars[lp] = chars[rp]
            chars[rp] = tmp
            lp++
            rp--
        }
    }

    private fun write(chars: CharArray, l: Int, r: Int) {
        for (i in l..r) outputStrBuffer!!.append(chars[i])
        outputStrBuffer!!.append("\n")
    }

    private fun hamilton(chars: CharArray, a: Int, b: Int, l: Int) {
        var dist = 0
        for (i in 0 until l) {
            dist += if (chars[a + i] == chars[b + i]) 0 else 1
        }
        outputStrBuffer!!.append(
            """
                $dist
                
                """.trimIndent()
        )
    }
}