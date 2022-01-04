package algorithms

class StringSimilarity {

/*
 * Complete the 'stringSimilarity' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts STRING s as parameter.
 */

    fun stringSimilarity(s: String): Long {
        val result = LongArray(s.length)
        var left = 0L
        var right = 0L
        result[0] = s.length.toLong()
        for (i in 1 until s.length){

            if (i <= right)
                result[i] = (right - i + 1).coerceAtMost(result[(i - left).toInt()])

            while (i + result[i] < s.length && s[result[i].toInt()] == s[i + result[i].toInt()])
                ++result[i]

            if (i + result[i] - 1 > right){
                left = i.toLong()
                right = i + result[i] - 1
            }
        }

        return result.sum()
    }
}