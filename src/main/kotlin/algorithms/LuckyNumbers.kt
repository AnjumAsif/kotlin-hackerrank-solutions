package algorithms

class LuckyNumbers {
    
    var dp = Array(20) { Array(200) { LongArray(2000) } }
    var vis = Array(20) { Array(200) { BooleanArray(2000) } }
    var nprime = BooleanArray(2010)

    fun buildPrime() {
        for (i in 2..2000) {
            if (!nprime[i]) {
                var j = 2 * i
                while (j <= 2000) {
                    nprime[j] = true
                    j += i
                }
            }
        }
        nprime[1] = true
        nprime[0] = nprime[1]
    }
    
    fun dp(n: Int, x: Int, y: Int): Long {
        if (n == 0) {
            return if (!nprime[x] && !nprime[y]) 1 else 0
        }
        if (vis[n][x][y]) return dp[n][x][y]
        vis[n][x][y] = true
        dp[n][x][y] = 0
        for (i in 0..9) {
            dp[n][x][y] += dp(n - 1, x + i, y + i * i)
        }
        return dp[n][x][y]
    }

    fun below(num: IntArray, offset: Int, len: Int, x: Int, y: Int): Long {
        if (len == 0) {
            return dp(0, x, y)
        }
        var result: Long = 0
        val current = num[offset]
        for (i in 0 until current) {
            result += dp(len - 1, x + i, y + i * i)
        }
        result += below(num, offset + 1, len - 1, x + current, y + current * current)
        return result
    }

    fun below(num: Long): Long {
        val n = num.toString()
        val arr = IntArray(n.length)
        for (i in n.indices) {
            arr[i] = n[i] - '0'
        }
        return below(arr, 0, arr.size, 0, 0)
    }

    fun luckyNumbers(a: Long, b: Long): Long {
        buildPrime()
        var a = a
        a--
        return below(b) - below(a)
    }
}