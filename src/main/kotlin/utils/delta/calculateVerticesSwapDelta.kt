package org.example.utils.delta

import org.example.utils.getVertexData

fun calculateVerticesSwapDelta(
    distanceMatrix: List<List<Int>>,
    i: Int,
    j: Int,
    route1: List<Int>,
    route2: List<Int>?
): Int {
    val newRoute2 = route2 ?: route1

    val (vertexBefore1, vertex1, vertexAfter1) = getVertexData(route1, i)
    val (vertexBefore2, vertex2, vertexAfter2) = getVertexData(newRoute2, j)

    return distanceMatrix[vertexBefore1][vertex2] +
            distanceMatrix[vertex2][vertexAfter1] +
            distanceMatrix[vertexBefore2][vertex1] +
            distanceMatrix[vertex1][vertexAfter2] -
            distanceMatrix[vertexBefore1][vertex1] -
            distanceMatrix[vertex1][vertexAfter1] -
            distanceMatrix[vertexBefore2][vertex2] -
            distanceMatrix[vertex2][vertexAfter2]
}