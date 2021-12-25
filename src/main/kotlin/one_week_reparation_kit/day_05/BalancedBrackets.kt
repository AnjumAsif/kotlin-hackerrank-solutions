package one_week_reparation_kit.day_05

import java.util.*

/*
 * Complete the 'isBalanced' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING s as parameter.
 */

fun isBalanced(s: String): String {
    val mapOfBrackets = mapOf(
        ']' to '[',
        ')' to '(',
        '}' to '{'
    )
    val openBrackets = arrayOf('[', '(', '{')
    val bracketStack = Stack<Char>()
    s.forEach {
        if (it in openBrackets) {
            bracketStack.push(it)
        } else {
            // {[(]
            if (bracketStack.isEmpty()) return "NO"
            val tmp = bracketStack.pop() // (
            if (tmp != mapOfBrackets[it]) return "NO"
        }
    }
    return if (bracketStack.isEmpty()) "YES" else "NO"
}
