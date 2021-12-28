package algorithms

/*
 * Complete the 'morganAndString' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts following parameters:
 *  1. STRING a
 *  2. STRING b
 */

fun morganAndString(a: String, b: String): String {
    return solveMorganAndString(a + "a", b + "b").let { it.filter { it != 'a' && it != 'b' } }
}


fun solveMorganAndString(a: String, b: String): String {
    val firstStringLength = a.length
    var i = 0
    val secondStringLength = b.length
    var j = 0

    val completedString = StringBuilder(firstStringLength + secondStringLength + 3)

    while (i < firstStringLength - 1 && j < secondStringLength - 1) {
        if (a[i] < b[j]) completedString.append(a[i++])
        else if (b[j] < a[i]) completedString.append(b[j++])
        else {
            var k = 0
            val e = a[i]
            while (e == a[i + k] && e == b[j + k]) k++;

            var r = 0
            while (a[i + k + r] == b[j + k + r]) r++;

            if (b[j + k + r] < a[i + k + r]) {
                completedString.append(b.substring(j, j + k))
                j += k
            } else {
                completedString.append(a.subSequence(i, i + k))
                i += k
            }
        }
    }
    for (k in i until firstStringLength) completedString.append(a[k])
    for (k in j until secondStringLength) completedString.append(b[k])
    return completedString.toString()
}