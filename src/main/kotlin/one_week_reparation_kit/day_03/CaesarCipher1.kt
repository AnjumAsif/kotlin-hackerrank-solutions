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
    
    var letter: Char
    val encryptedString = CharArray(s.length)
    for ((index, c) in s.withIndex()) {
        if (c in 'A'..'Z') {
            letter = c + offset
            if (letter > 'Z') letter -= 26
        }
        else if (c in 'a'..'z') {
            letter = c + offset
            if (letter > 'z') letter -= 26
        }
        else
            letter = c
        encryptedString[index] = letter
    }
    
    return encryptedString.joinToString("")
}

fun main() {

    println(caesarCipher("middle-Outz", 2))

}