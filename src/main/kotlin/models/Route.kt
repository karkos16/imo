package org.example.models

data class Route(
    val indices: MutableList<Int>,
    val distance: Int
) {
    fun checkEdgeExistence(startVertex: Int, endVertex: Int): EdgeExistence {
        val startIndex = indices.indexOf(startVertex)
        val endIndex = indices.indexOf(endVertex)
        return when {
            startIndex == -1 || endIndex == -1 -> EdgeExistence.NOT_EXIST
            indices.dropLast(1).getOrElse(startIndex + 1) { indices[0] } == endVertex -> EdgeExistence.EXIST
            indices.dropLast(1).getOrElse(endIndex + 1) { indices[0] } == startVertex -> EdgeExistence.REVERSED
            else -> EdgeExistence.NOT_EXIST
        }
    }
}

enum class EdgeExistence {
    EXIST,
    REVERSED,
    NOT_EXIST
}
