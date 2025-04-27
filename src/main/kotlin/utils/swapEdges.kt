package org.example.utils

fun <T> MutableList<T>.swapEdges(i: Int, j: Int) {
    var start = i
    var end = j

    if (j < i) {
        start = j
        end = i
    }

    if (start < 0 || end < 0 || start + 1 >= size || end + 1 > size || start >= end - 1) {
        return
    }

    val subListToReverse = this.subList(start + 1, end + 1)
    subListToReverse.reverse()
}