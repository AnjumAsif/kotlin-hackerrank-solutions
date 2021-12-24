package one_week_reparation_kit.day_02

/*
 * Complete the 'lonelyinteger' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts INTEGER_ARRAY a as parameter.
 */

fun lonelyinteger(a: Array<Int>): Int {
    // Write your code here
    var result = 0

    for (number in a) {
        result = result xor number
    }

    return result

}

/*
  Explanation algorithm:
  1) Any number xor with itself will give zero.
  2) Any number xor with zero will give the number.
  3) There is an odd number of numbers in the array and they are all pairs of the same number, apart from one
 */