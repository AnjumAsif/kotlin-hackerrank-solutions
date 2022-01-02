package mathemetics

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter


class FunWith1010 {
    fun main(args: Array<String>) {
        Modulo.fact[0] = 1
        for (i in 1 until Modulo.MOD) {
            Modulo.fact[i.toInt()] = Modulo.fact[(i - 1).toInt()] * i % Modulo.MOD
        }
        if (Modulo.fact[(Modulo.MOD - 1).toInt()] != Modulo.MOD - 1) {
            throw AssertionError()
        }

        val out = PrintWriter(System.out)
        solve(Input(BufferedReader(InputStreamReader(System.`in`))), out)
        out.close()
    }

    fun solve(input: Input, out: PrintWriter) {
        val tests = input.nextInt()
        for (test in 0 until tests) {
            val n = input.nextLong()
            val m = input.nextLong()
            out.println(Modulo.solve(n, m))
        }
    }

    class Modulo(var pow: Long, var r: Long) {
        companion object {
            val MOD: Long = 2000003
            var fact: LongArray = LongArray(MOD.toInt())
            fun modPow(x: Long, pow: Long): Long {
                var x = x
                var pow = pow
                var r: Long = 1
                while (pow > 0) {
                    if (pow % 2 == 1L) {
                        r = r * x % MOD
                    }
                    pow /= 2
                    x = x * x % MOD
                }
                return r
            }

            fun modInv(x: Long): Long {
                return modPow(x, MOD - 2)
            }

            fun fact(n: Long): Modulo {
                var n = n
                val ret = Modulo(0, 1L)
                while (n > 0) {
                    ret.r = ret.r * fact[(n % MOD).toInt()] % MOD
                    if (n / MOD % 2 == 1L) {
                        ret.r = (MOD - ret.r) % MOD
                    }
                    ret.pow += n / MOD
                    n /= MOD
                }
                return ret
            }

            fun c(n: Long, k: Long): Modulo {
                return fact(n).mul(fact(k).mul(fact(n - k)).inv())
            }

            fun solve(n: Long, m: Long): Long {
                var ret = f(n - 1)
                val base =
                    (modPow(2, 3 * (n - 1)) + 3 * (MOD - modPow(2, 2 * (n - 1))) + 3 * modPow(2, n - 1) + MOD - 1) % MOD
                ret = (ret + base * ((m - n + 1) % MOD)) % MOD
                ret = (ret + (MOD - 3) * g(n - 1)) % MOD
                ret = (ret + 3 * h(n - 1)) % MOD
                ret = (ret + (MOD - n % MOD)) % MOD // +1 -3 +3
                return ret
            }

            fun f(n: Long): Long {
                return ((n + 2) % MOD * modPow(2, 3 * n - 1) % MOD + (MOD - 3) * modPow(
                    2, (n + (MOD - 1) - 2) % (MOD - 1)
                ) % MOD * (n % MOD) % MOD * c(2 * n, n).value()) % MOD
            }

            fun g(n: Long): Long {
                return ((n + 2) % MOD * modPow(2, 2 * n - 1) % MOD + (MOD - n % MOD) * modInv(2) % MOD * c(
                    2 * n, n
                ).value()) % MOD
            }

            fun h(n: Long): Long {
                return (n + 2) % MOD * modPow(2, n - 1) % MOD
            }
        }

        fun value(): Long {
            if (pow < 0) {
                throw AssertionError()
            }
            return if (pow > 0) 0 else r
        }

        fun mul(o: Modulo): Modulo {
            return Modulo(pow + o.pow, r * o.r % 2000003)
        }

        fun inv(): Modulo {
            return Modulo(-pow, modInv(r))
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


        fun nextLong(): Long {
            return next()!!.toLong()
        }

    }
}