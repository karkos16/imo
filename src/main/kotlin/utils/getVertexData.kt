package org.example.utils

fun getVertexData(route: List<Int>, index: Int): Triple<Int, Int, Int> {
    val vertex = route[index]
    val vertexBefore = if (index == 0) route.last() else route[index - 1]
    val vertexAfter = if (index == route.size - 1) route[0] else route[index + 1]
    return Triple(vertexBefore, vertex, vertexAfter)
}