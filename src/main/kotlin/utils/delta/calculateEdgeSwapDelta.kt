package org.example.utils.delta

import org.example.utils.getVertexData

fun calculateEdgeSwapDelta(
    distanceMatrix: List<List<Int>>,
    i: Int,
    j: Int,
    route1: List<Int>
): Int {
    val (_, vertex1, vertexAfter1) = getVertexData(route1, i)
    val (_, vertex2, vertexAfter2) = getVertexData(route1, j)

    return distanceMatrix[vertex1][vertex2] +
            distanceMatrix[vertexAfter1][vertexAfter2] -
            distanceMatrix[vertex1][vertexAfter1] -
            distanceMatrix[vertex2][vertexAfter2]
}