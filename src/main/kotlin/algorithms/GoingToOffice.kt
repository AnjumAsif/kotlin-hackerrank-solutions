package algorithms

import java.util.*
import kotlin.math.abs


class GoingToOffice {


    class Edge(var from: Int, var to: Int, var weight: Long) : Comparable<Edge> {
        override fun compareTo(other: Edge): Int {
            if (weight < other.weight) return -1
            return if (weight > other.weight) 1 else 0
        }
    }

    val INFINITY = Long.MAX_VALUE shr 2
    var N = 0
    var M: Int = 0
    var source: Int = 0
    var sink: Int = 0
    var Q: Int = 0
    lateinit var g: Array<ArrayList<Edge>?>
    lateinit var q: Array<IntArray>

    fun read() {
        val sc = Scanner(System.`in`)
        N = sc.nextInt()
        M = sc.nextInt()
        g = arrayOfNulls(N)
        for (i in 0 until N) {
            g[i] = ArrayList()
        }
        for (i in 0 until M) {
            val from = sc.nextInt()
            val to = sc.nextInt()
            val w = sc.nextLong()
            g[from]!!.add(Edge(from, to, w))
            g[to]!!.add(Edge(to, from, w))
        }
        source = sc.nextInt()
        sink = sc.nextInt()
        Q = sc.nextInt()
        q = Array(Q) { IntArray(2) }
        for (i in 0 until Q) {
            val from = sc.nextInt()
            val to = sc.nextInt()
            q[i][0] = from.coerceAtMost(to)
            q[i][1] = from.coerceAtLeast(to)
        }
        sc.close()
    }

    fun main(args: Array<String>) {
        read()
        val sourcePT = IntArray(N)
        val sourceTreeDist = LongArray(N)
        dijkstra(g, source, sourcePT, sourceTreeDist)
        if (sourceTreeDist[sink] >= INFINITY) {
            for (i in 0 until Q) {
                println("Infinity")
            }
            return
        }
        val sinkPT = IntArray(N)
        val sinkTreeDist = LongArray(N)
        dijkstra(g, sink, sinkPT, sinkTreeDist)
        val path = getPath(sourcePT, source, sink)
        val flag = BooleanArray(N)
        for (v in path) flag[v] = true
        val vMap = IntArray(N)
        Arrays.fill(vMap, -1)
        for (i in path.indices) {
            vMap[path[i]] = i
        }
        for (i in 0 until N) {
            if (flag[i]) continue
            var j: Int
            j = sourcePT[i]
            while (j != -1 && !flag[j]) {
                j = sourcePT[j]
            }
            if (j != -1) {
                vMap[i] = vMap[j]
            }
        }
        val alt = LongArray(path.size - 1)
        Arrays.fill(alt, INFINITY)
        val pq = PriorityQueue<Edge>()
        for (i in 0 until N) {
            for (e in g[i]!!) {
                if (vMap[e.from] != -1 && (vMap[e.to] > vMap[e.from] + 1 || vMap[e.to] > vMap[e.from] && !flag[e.to])) {
                    pq.add(Edge(vMap[e.from], vMap[e.to], e.weight + sourceTreeDist[e.from] + sinkTreeDist[e.to]))
                }
            }
        }
        val fw = IntArray(path.size - 1)
        Arrays.fill(fw, -1)
        run {
            val i = 0
            while (i < path.size - 1 && !pq.isEmpty()) {
                val e = pq.remove()
                var j = e.from
                while (j < e.to) {
                    if (fw[j] == -1) {
                        fw[j] = e.to
                        alt[j] = e.weight
                        j++
                    } else {
                        j = fw[j]
                    }
                }
            }
        }
        for (i in 0 until Q) {
            var flag: Long = if (!flag[q[i][0]] || !flag[q[i][1]] || abs(vMap[q[i][0]] - vMap[q[i][1]]) != 1) {
                sourceTreeDist[sink]
            } else {
                alt[vMap[q[i][0]].coerceAtMost(vMap[q[i][1]])]
            }
            if (flag >= INFINITY) {
                println("Infinity")
            } else {
                println(flag)
            }
        }
    }

    private fun getPath(parents: IntArray, source: Int, sink: Int): ArrayList<Int> {
        var n = sink
        val ints = ArrayList<Int>()
        val rev = ArrayList<Int>()
        while (n != source) {
            rev.add(n)
            n = parents[n]
        }
        rev.add(source)
        for (i in rev.indices.reversed()) {
            ints.add(rev[i])
        }
        return ints
    }

    private fun dijkstra(g: Array<ArrayList<Edge>?>, start: Int, parents: IntArray, dist: LongArray) {
        Arrays.fill(parents, -1)
        Arrays.fill(dist, INFINITY)
        val flag = BooleanArray(g.size)
        flag[start] = true
        dist[start] = 0
        val pq = PriorityQueue<Edge>()
        for (e in g[start]!!) {
            pq.add(e)
        }
        var i = 0
        while (i < parents.size - 1 && !pq.isEmpty()) {
            var e = pq.remove()
            while (flag[e.to] && !pq.isEmpty()) {
                e = pq.remove()
            }
            if (flag[e.to]) break
            dist[e.to] = e.weight
            flag[e.to] = true
            parents[e.to] = e.from
            for (next in g[e.to]!!) {
                if (!flag[next.to]) {
                    pq.add(Edge(next.from, next.to, next.weight + dist[e.to]))
                }
            }
            i++
        }
    }
}