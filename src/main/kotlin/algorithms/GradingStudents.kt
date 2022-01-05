package algorithms


/*
 * Complete the 'gradingStudents' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts INTEGER_ARRAY grades as parameter.
 */

fun gradingStudents(grades: Array<Int>): Array<Int> {
    return grades.map {
        if (it >= 38 && it % 5 > 2) it + 5 - it % 5
        else it
    }.toTypedArray()
}