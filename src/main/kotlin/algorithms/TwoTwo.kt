package algorithms

import java.math.BigInteger

/*
 * Complete the 'twoTwo' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts STRING a as parameter.
 */

class Group (val ok: Boolean, val m: Array<Group?> = arrayOfNulls(10))

val powerOf2List = mutableListOf<String>().also {
    var v = BigInteger.ONE
    val v2 = BigInteger.valueOf(2)
    for(i in 0..800) {
        it.add(v.toString())
        v = v.multiply(v2)
    }
}

fun add(a: Group, s: String, startIndex: Int) {
    val digit = s[startIndex] - '0'
    if (a.m[digit] == null) {
        a.m[digit] = Group(startIndex == s.length - 1)
    }
    if (startIndex < s.length - 1) {
        add(a.m[digit]!!, s, startIndex + 1)
    }
}

val group = Group(false).also {
    for (s in powerOf2List) {
        add(it, s, 0)
    }
}


/*
 * Complete the twoTwo function below.
 */
fun twoTwo(a: String): Int {
    var result = 0
    val digits = a.toCharArray().map { it - '0' }.toIntArray()
    val size = digits.size

    for(i in 0 until size) {
        var arr: Group = group

        var j = i
        while (j < size) {
            val digit = digits[j]
            val nextArr = arr.m[digit] ?: break
            if (nextArr.ok) {
                result ++
            }
            j++
            arr = nextArr
        }
    }

    return result
}