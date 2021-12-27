package skills_certification_test.problem_solving.basic

/*
 * Complete the 'mostBalancedPartition' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER_ARRAY parent
 *  2. INTEGER_ARRAY files_size
 */
fun mostBalancedPartition(parent: Array<Int>, fileSizes: Array<Int>): Int {
    val graph = DirectedGraph()
    for (index in parent.indices) {
        graph.addVertex(index, fileSizes[index])
        if (index != 0) {
            graph.addEdge(parent[index], index)
        }
    }
    return graph.mostBalancedPartition()
}


class DirectedGraph {
    var vertices: MutableMap<Int, Vertex> = HashMap()
    var diskSize: Long = 0

    class Vertex constructor(val fileSize: Int) {
        var totalMemoryRequired: Long = 0
        var edges: MutableSet<Vertex?> = HashSet()
        fun addEdge(to: Vertex?) {
            edges.add(to)
        }
    }

    fun addVertex(data: Int, fileSize: Int) {
        vertices[data] = Vertex(fileSize)
    }

    fun addEdge(from: Int, to: Int) {
        val fromVertex = vertices[from]
        fromVertex!!.addEdge(vertices[to])
    }

    fun mostBalancedPartition(): Int {
        computeFileDataDistribution()
        return mostBalancedPartition(vertices[0])
    }

    private fun computeFileDataDistribution() {
        diskSize = computeFileDataDistribution(vertices[0])
    }

    private fun computeFileDataDistribution(current: Vertex?): Long {
        var totalMemoryRequired = current!!.fileSize.toLong()
        for (child in current.edges) {
            totalMemoryRequired += computeFileDataDistribution(child)
        }
        current.totalMemoryRequired = totalMemoryRequired
        return current.totalMemoryRequired
    }

    private fun mostBalancedPartition(current: Vertex?): Int {
        var result = Int.MAX_VALUE
        for (child in current!!.edges) {
            result = result.coerceAtMost(mostBalancedPartition(child))
            result = result.toLong().coerceAtMost(Math.abs(diskSize - 2 * child!!.totalMemoryRequired)).toInt()
        }
        return result
    }
}