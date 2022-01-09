package data_structures

import java.util.*

class AlmostSortedInterval {
    fun solve(arr: Array<Int>): Long {
        val length = arr.size
        val rightClosedSmall = IntArray(length)
        val leftClosedBig = IntArray(length)
        var stack = Stack<Int>()
        for (i in length - 1 downTo 0) {
            while (!stack.empty() && arr[stack.peek()] >= arr[i]) {
                stack.pop()
            }
            if (stack.empty()) {
                rightClosedSmall[i] = length
            } else {
                rightClosedSmall[i] = stack.peek()
            }
            stack.push(i)
        }
        stack = Stack()
        for (i in 0 until length) {
            while (!stack.empty() && arr[stack.peek()] <= arr[i]) {
                stack.pop()
            }
            if (stack.empty()) {
                leftClosedBig[i] = -1
            } else {
                leftClosedBig[i] = stack.peek()
            }
            stack.push(i)
        }
        val intervals: Array<ArrayList<Int>?> = arrayOfNulls(length)
        for (i in 0 until length) {
            intervals[i] = ArrayList()
        }
        val tree = BitIndexTree(length + 1)
        var count: Long = 0
        for (i in length - 1 downTo 0) {
            tree.update(i + 1, 1)
            if (leftClosedBig[i] >= 0) {
                intervals[leftClosedBig[i]]?.add(i)
            }
            for (j in intervals[i]!!) {
                tree.update(j + 1, -1)
            }
            count += (tree.read(rightClosedSmall[i]) - tree.read(i)).toLong()
        }
        return count
    }

    class BitIndexTree(MaxVal: Int) {
        private var maxVal = 0
        private var tree: IntArray? = null

        init {
            assert(MaxVal > 0)
            this.maxVal = MaxVal
            tree = IntArray(MaxVal + 1)
        }

        fun update(idx: Int, value: Int) {
            var index = idx
            assert(index > 0)
            while (index <= maxVal) {
                tree!![index] += value
                index += index and -index
            }
        }

        fun read(idx: Int): Int {
            var index = idx
            var sum = 0
            while (index > 0) {
                sum += tree!![index]
                index -= index and -index
            }
            return sum
        }
    }
}