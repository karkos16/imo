package org.example.utils

fun <T> MutableList<T>.swapEdges(i: Int, j: Int) {
    if (i < 0 || j < 0 || i + 1 >= size || j + 1 >= size || i >= j - 1) {
        return
    }

    val subListToReverse = this.subList(i + 1, j + 1)
    subListToReverse.reverse()
}