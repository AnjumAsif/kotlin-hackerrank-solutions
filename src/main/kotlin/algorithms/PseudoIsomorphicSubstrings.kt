import java.util.*

fun main(args: Array<String>) {
    val line = readLine()!!
    val charsLine = line.toCharArray()
    val idxMinusPrevOccurenceIdx = IntArray(line.length)
    val prev: MutableMap<Char, Int> = HashMap()
    for (i in line.indices) {
        if (prev.containsKey(charsLine[i])) {
            idxMinusPrevOccurenceIdx[i] = i - prev[charsLine[i]]!!
        }
        prev[charsLine[i]] = i
    }
    PseudoIsomorphicSubstrings(idxMinusPrevOccurenceIdx)
}

@Suppress("NAME_SHADOWING")
class PseudoIsomorphicSubstrings(private val values: IntArray) {
    class Node(val valuesFromRootCount: Int) {
        val edges: MutableMap<Int?, Edge>
        var slink: Node? = null

        init {
            edges = HashMap()
        }
    }

    class Edge(val suffixStartIndex: Int, val from: Int, val to: Int, val next: Node?) {

        val length: Int
            get() = to - from
    }

    class ActivePoint(
        private val activeNode: Node?, private val activeEdgeFirstValue: Int?, private val activeLength: Int
    ) {

        private val activeEdge: Edge?
            get() = activeNode!!.edges[activeEdgeFirstValue]

        private fun getActiveValue(values: IntArray): Int {
            val valueOnEdgeIndex = activeEdge!!.from + activeLength
            return getSuffixValue(activeEdge!!.suffixStartIndex, values, valueOnEdgeIndex)
        }

        fun getSuffixValue(suffixStartIndex: Int, values: IntArray, valueIndex: Int): Int {
            return if (suffixStartIndex <= valueIndex - values[valueIndex]) values[valueIndex] else 0
        }

        fun pointsToActiveNode(): Boolean {
            return activeLength == 0
        }

        fun activeNodeIs(node: Node): Boolean {
            return activeNode === node
        }

        fun activeNodeHasEdgeStartingWith(value: Int): Boolean {
            return activeNode!!.edges.containsKey(value)
        }

        fun activeNodeHasSlink(): Boolean {
            return activeNode!!.slink != null
        }

        fun pointsToOnActiveEdge(values: IntArray, value: Int): Boolean {
            return getActiveValue(values) == value
        }

        fun pointsToTheEndOfActiveEdge(): Boolean {
            return activeEdge!!.length == activeLength
        }

        fun pointsAfterTheEndOfActiveEdge(): Boolean {
            return activeEdge!!.length < activeLength
        }

        fun moveToEdgeStartingWithAndByOne(value: Int): ActivePoint {
            return ActivePoint(activeNode, value, 1)
        }

        fun moveToNextNodeOfActiveEdge(): ActivePoint {
            return ActivePoint(activeEdge!!.next, null, 0)
        }

        fun moveToSlink(
            values: IntArray, suffixStartIndex: Int, index: Int
        ): ActivePoint {
            val slinkIndex = suffixStartIndex + activeNode!!.slink!!.valuesFromRootCount
            val slinkValue = getSuffixValue(suffixStartIndex, values, slinkIndex)
            val slinkActiveLength = index - slinkIndex
            return ActivePoint(
                activeNode.slink, slinkValue, slinkActiveLength
            )
        }

        fun moveTo(node: Node?): ActivePoint {
            return ActivePoint(node, null, 0)
        }

        fun moveTo(node: Node?, activeLength: Int): ActivePoint {
            return ActivePoint(node, 0, activeLength)
        }

        fun moveByOneValue(): ActivePoint {
            return ActivePoint(
                activeNode, activeEdgeFirstValue, activeLength + 1
            )
        }

        fun moveToNextNodeOfActiveEdge(
            values: IntArray, suffixStartIndex: Int
        ): ActivePoint {
            val valueAtTheEndOfEdgeIndex = suffixStartIndex + activeNode!!.valuesFromRootCount + activeEdge!!.length
            val valueAtTheEndOfEdge = getSuffixValue(suffixStartIndex, values, valueAtTheEndOfEdgeIndex)
            return ActivePoint(
                activeEdge!!.next, valueAtTheEndOfEdge, activeLength - activeEdge!!.length
            )
        }

        fun addEdgeToActiveNode(value: Int, edge: Edge) {
            activeNode!!.edges[value] = edge
        }

        fun splitActiveEdge(
            values: IntArray, suffixStartIndex: Int, index: Int, value: Int
        ): Node {
            val newNode = Node(
                activeNode!!.valuesFromRootCount + activeLength
            )
            val activeEdgeToSplit = activeEdge
            val splitEdge = Edge(
                activeEdgeToSplit!!.suffixStartIndex,
                activeEdgeToSplit.from,
                activeEdgeToSplit.from + activeLength,
                newNode
            )
            newNode.edges[getActiveValue(values)] = Edge(
                activeEdgeToSplit.suffixStartIndex,
                activeEdgeToSplit.from + activeLength,
                activeEdgeToSplit.to,
                activeEdgeToSplit.next
            )
            newNode.edges[value] = Edge(suffixStartIndex, index, values.size, null)
            activeNode.edges[activeEdgeFirstValue] = splitEdge
            return newNode
        }

        fun setSlinkTo(
            previouslyAddedNodeOrAddedEdgeNode: Node?, node: Node?
        ): Node? {
            if (previouslyAddedNodeOrAddedEdgeNode != null) previouslyAddedNodeOrAddedEdgeNode.slink = node
            return node
        }

        fun setSlinkToActiveNode(previouslyAddedNodeOrAddedEdgeNode: Node?): Node? {
            return setSlinkTo(previouslyAddedNodeOrAddedEdgeNode, activeNode)
        }
    }

    private val root: Node = Node(0)
    private var activePoint: ActivePoint
    private var remainder: Int
    private var totalLeavesCount: Long
    private var leavesSum: Long

    init {
        activePoint = ActivePoint(this.root, null, 0)
        remainder = 0
        totalLeavesCount = 0
        leavesSum = 0
        build()
    }

    private fun build() {
        for (i in values.indices) {
            add(i)
        }
    }

    private fun add(index: Int) {
        remainder++
        var valueFoundInTheTree = false
        var previouslyAddedNodeOrAddedEdgeNode: Node? = null
        while (!valueFoundInTheTree && remainder > 0) {
            val suffixStartIndex = index - remainder + 1
            val valueToCheckExistenceInTree = activePoint.getSuffixValue(suffixStartIndex, values, index)
            if (activePoint.pointsToActiveNode()) {
                if (activePoint.activeNodeHasEdgeStartingWith(valueToCheckExistenceInTree)) {
                    activeNodeHasEdgeStartingWithValue(
                        valueToCheckExistenceInTree, previouslyAddedNodeOrAddedEdgeNode
                    )
                    valueFoundInTheTree = true
                } else {
                    if (activePoint.activeNodeIs(this.root)) {
                        rootNodeHasNotEdgeStartingWithValue(suffixStartIndex, index, valueToCheckExistenceInTree)
                    } else {
                        previouslyAddedNodeOrAddedEdgeNode = internalNodeHasNotEdgeStartingWithValue(
                            suffixStartIndex, index, valueToCheckExistenceInTree, previouslyAddedNodeOrAddedEdgeNode
                        )
                    }
                }
            } else {
                if (activePoint.pointsToOnActiveEdge(values, valueToCheckExistenceInTree)) {
                    activeEdgeHasValue()
                    valueFoundInTheTree = true
                } else {
                    previouslyAddedNodeOrAddedEdgeNode = if (activePoint.activeNodeIs(this.root)) {
                        edgeFromRootNodeHasNotValue(
                            suffixStartIndex, index, valueToCheckExistenceInTree, previouslyAddedNodeOrAddedEdgeNode
                        )
                    } else {
                        edgeFromInternalNodeHasNotValue(
                            suffixStartIndex, index, valueToCheckExistenceInTree, previouslyAddedNodeOrAddedEdgeNode
                        )
                    }
                }
            }
        }
        leavesSum += totalLeavesCount
        println(leavesSum)
    }

    private fun activeNodeHasEdgeStartingWithValue(
        value: Int, previouslyAddedNodeOrAddedEdgeNode: Node?
    ) {
        activePoint.setSlinkToActiveNode(previouslyAddedNodeOrAddedEdgeNode)
        activePoint = activePoint.moveToEdgeStartingWithAndByOne(value)
        if (activePoint.pointsToTheEndOfActiveEdge()) {
            activePoint = activePoint.moveToNextNodeOfActiveEdge()
        }
    }

    private fun rootNodeHasNotEdgeStartingWithValue(suffixStartIndex: Int, index: Int, value: Int) {
        activePoint.addEdgeToActiveNode(value, Edge(suffixStartIndex, index, values.size, null))
        totalLeavesCount++
        activePoint = activePoint.moveTo(this.root)
        remainder--
        assert(remainder == 0)
    }

    private fun internalNodeHasNotEdgeStartingWithValue(
        suffixStartIndex: Int, index: Int, value: Int, previouslyAddedNodeOrAddedEdgeNode: Node?
    ): Node? {
        var previouslyAddedNodeOrAddedEdgeNode = previouslyAddedNodeOrAddedEdgeNode
        activePoint.addEdgeToActiveNode(value, Edge(suffixStartIndex, index, values.size, null))
        totalLeavesCount++
        previouslyAddedNodeOrAddedEdgeNode = activePoint.setSlinkToActiveNode(
            previouslyAddedNodeOrAddedEdgeNode
        )
        activePoint = if (activePoint.activeNodeHasSlink()) {
            activePoint.moveToSlink(values, suffixStartIndex + 1, index)
        } else {
            activePoint.moveTo(this.root, remainder - 2)
        }
        activePoint = walkDown(suffixStartIndex + 1)
        remainder--
        return previouslyAddedNodeOrAddedEdgeNode
    }

    private fun activeEdgeHasValue() {
        activePoint = activePoint.moveByOneValue()
        if (activePoint.pointsToTheEndOfActiveEdge()) {
            activePoint = activePoint.moveToNextNodeOfActiveEdge()
        }
    }

    private fun edgeFromRootNodeHasNotValue(
        suffixStartIndex: Int, index: Int, value: Int, previouslyAddedNodeOrAddedEdgeNode: Node?
    ): Node? {
        var previouslyAddedNodeOrAddedEdgeNode = previouslyAddedNodeOrAddedEdgeNode
        val newNode = activePoint.splitActiveEdge(
            values, suffixStartIndex, index, value
        )
        totalLeavesCount++
        previouslyAddedNodeOrAddedEdgeNode = activePoint.setSlinkTo(
            previouslyAddedNodeOrAddedEdgeNode, newNode
        )
        activePoint = activePoint.moveTo(this.root, remainder - 2)
        activePoint = walkDown(suffixStartIndex + 1)
        remainder--
        return previouslyAddedNodeOrAddedEdgeNode
    }

    private fun edgeFromInternalNodeHasNotValue(
        suffixStartIndex: Int, index: Int, value: Int, previouslyAddedNodeOrAddedEdgeNode: Node?
    ): Node? {
        var previouslyAddedNodeOrAddedEdgeNode = previouslyAddedNodeOrAddedEdgeNode
        val newNode = activePoint.splitActiveEdge(
            values, suffixStartIndex, index, value
        )
        totalLeavesCount++
        previouslyAddedNodeOrAddedEdgeNode = activePoint.setSlinkTo(
            previouslyAddedNodeOrAddedEdgeNode, newNode
        )
        activePoint = if (activePoint.activeNodeHasSlink()) {
            activePoint.moveToSlink(values, suffixStartIndex + 1, index)
        } else {
            activePoint.moveTo(this.root, remainder - 2)
        }
        activePoint = walkDown(suffixStartIndex + 1)
        remainder--
        return previouslyAddedNodeOrAddedEdgeNode
    }

    private fun walkDown(suffixStartIndex: Int): ActivePoint {
        while ((!activePoint.pointsToActiveNode() && (activePoint.pointsToTheEndOfActiveEdge() || activePoint.pointsAfterTheEndOfActiveEdge()))) {
            activePoint = if (activePoint.pointsAfterTheEndOfActiveEdge()) {
                activePoint.moveToNextNodeOfActiveEdge(values, suffixStartIndex)
            } else {
                activePoint.moveToNextNodeOfActiveEdge()
            }
        }
        return activePoint
    }
}