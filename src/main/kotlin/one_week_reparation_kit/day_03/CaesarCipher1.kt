package one_week_reparation_kit.day_03


/*
 * Complete the 'caesarCipher' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts following parameters:
 *  1. STRING s
 *  2. INTEGER k
 */

fun caesarCipher(s: String, k: Int): String {
    // Write your code here

    val offset = k % 26
    if (offset == 0) return s

    return s.toList().map { ch ->
        if(ch in 'a'..'z') {
            var t = ch + offset
            if (t>'z') t -= 26
            t
        } else if (ch in 'A'..'Z') {
            var t = ch + offset
            if (t>'Z') t -= 26
            t
        } else {
            ch
        }
    }.joinToString("")
}

fun main() {

    println(caesarCipher("middle-Outz", 2))

}