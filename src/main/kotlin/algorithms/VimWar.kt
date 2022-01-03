package algorithms

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*


class VimWar {
    /*
 * Complete the 'vimWar' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. STRING_ARRAY skills
 *  2. STRING requirement



    fun countOff(soldiers: IntArray, requirement: Int, M: Int): Int {
        val N = soldiers.size // number of soldiers to select from
        val armyCounts = IntArray(N + 1) // 2**k - 1 values
        run {
            var i = 0
            while (i++ < N) {
                armyCounts[i] = ((armyCounts[i - 1] shl 1) + 1) % MOD
            }
        }
        // ---------------
        val skillMasksCount = 1 shl M
        val allSkillsMask = skillMasksCount - 1
        val unskilledSoldiers = IntArray(skillMasksCount) // all zeroes
        // populate unskilledSoldiers array... O(N) operation... from "memory optimized, super easy to code"
        // filter "extra" skill warriors
        val extraSkills = requirement.inv() and allSkillsMask

        var fighter = 0

        while (fighter < soldiers.size) {
            if (soldiers[fighter] and extraSkills > 0) continue  // over-skilled combatant
            // grant extra skills
            fighter = fighter or extraSkills
            unskilledSoldiers[fighter]++
        }


//        for (fighter in soldiers) {
//            if (fighter and extraSkills > 0) continue  // over-skilled combatant
//            // grant extra skills
//            fighter = fighter or extraSkills
//            unskilledSoldiers[fighter]++
//        }


        // init sign array
        val sign = IntArray(2)
        sign[0] = if (M % 2 == 0) 1 else -1
        sign[1] = -sign[0]
        // now O(M*2**M) loop... from "memory optimized, super easy to code"
        for (i in 0 until M) for (mask in 0 until skillMasksCount) {
            if (mask and (1 shl i) > 0) unskilledSoldiers[mask] += unskilledSoldiers[mask xor (1 shl i)]
        }
        // accumulate result
        var result = 0
        for (mask in 0 until skillMasksCount) {
            // update result
            val oddness = BitSet.valueOf(longArrayOf(mask.toLong())).cardinality() and 1
            val extraArmiesCount = (MOD + sign[oddness] * armyCounts[unskilledSoldiers[mask]]) % MOD
            result = (result + extraArmiesCount) % MOD
        }
        return result
    }

    fun vimWar(skills: Array<String>, requirement: String): Int {
        var skillsRequirement = requirement
        val solders = IntArray(skills.size)
        for (i in skills.indices) {
            solders[i] = skills[i].trim { it <= ' ' }.toInt(2)
        }
        skillsRequirement = skillsRequirement.trim { it <= ' ' }
        val M = skillsRequirement.length
        val req = skillsRequirement.trim { it <= ' ' }.toInt(2)

        //int count = countON(solders, requirement);
        return countOff(solders, req, M)
    }
*/

    var MOD = 1000000007L
    var maxN = 100000
    var maxM = 20

    var n = 0
    var m = 0
    var k = 0 // number of required skills

    var f = Array(1 shl maxM) { IntArray(maxM + 1) }
    var originalSkill = IntArray(maxN)

    fun power(u: Long, v: Int): Long {
        var x = u
        var n = v
        var result = 1L
        while (n > 0) {
            if (n % 2 != 0) {
                result = result * x % MOD
            }
            x = x * x % MOD
            n = n shr 1
        }
        return result
    }

    fun main(args: Array<String>) {
        val br = BufferedReader(InputStreamReader(System.`in`))
        val st = StringTokenizer(br.readLine())
        n = st.nextToken().toInt()
        m = st.nextToken().toInt()

        for (i in 0 until n) {
            val s = br.readLine()
            var value = 0
            for (j in 0 until m) {
                value = value * 2 + (s[j] - '0')
            }
            originalSkill[i] = value
        }

        val s = br.readLine()
        var target = 0
        for (j in 0 until m) {
            if (s[j] == '1') {
                target = 2 * target + 1
                k++
            }
        }

        for (i in 0 until n) {
            var value = 0
            var flag = true
            for (j in m - 1 downTo 0) {
                if (s[m - 1 - j] == '1') {
                    value = if (originalSkill[i] and (1 shl j) != 0) value * 2 + 1 else value * 2
                } else {
                    if (originalSkill[i] and (1 shl j) != 0) {
                        flag = false
                        break
                    }
                }
            }
            if (flag) f[value][k]++
        }

        for (j in k downTo 1) {
            for (i in 0 until (1 shl k)) {
                f[i][j - 1] += f[i][j]
                val value = i and (1 shl k - j)
                if (value != 0) {
                    f[i][j - 1] += f[i xor (1 shl k - j)][j]
                }
            }
        }

        var result = 0L
        for (i in 0 until (1 shl k)) {
            var cnt = 0
            for (j in 0 until k) {
                if (i and (1 shl j) != 0) cnt++
            }
            if (cnt % 2 == k % 2) result += power(2L, f[i][0]) else result -= power(2L, f[i][0])
            result %= MOD
        }
        if (target == 0) result -= 1
        result = (result + MOD) % MOD
        println(result)
        br.close()
    }
}