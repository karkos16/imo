package org.example.utils

fun <T> MutableList<T>.swapIndices(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}