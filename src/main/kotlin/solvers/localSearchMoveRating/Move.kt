package org.example.solvers.localSearchMoveRating

import org.example.models.MoveType

data class Move(
    val moveType: MoveType,
    val vertex1: Int,
    val successor1: Int,
    val predecessor1: Int?,
    val vertex2: Int,
    val successor2: Int,
    val predecessor2: Int?,
    val delta: Int
) {
    override fun hashCode(): Int {
        var result = vertex1
        result = 31 * result + successor1
        result = 31 * result + vertex2
        result = 31 * result + successor2
        predecessor1?.let {result = 31 * result + predecessor1}
        predecessor2?.let {result = 31 * result + predecessor2}
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Move

        if (moveType != other.moveType) return false
        if (vertex1 != other.vertex1) return false
        if (successor1 != other.successor1) return false
        if (vertex2 != other.vertex2) return false
        if (successor2 != other.successor2) return false
        if (predecessor1 != other.predecessor1) return false
        if (predecessor2 != other.predecessor2) return false

        return true
    }

    fun reverseEdge(): Move {
        return if (predecessor1 != null && predecessor2 != null) {
            this.copy(
                predecessor1 = successor1,
                vertex1 = vertex1,
                successor1 = predecessor1,
                predecessor2 = successor2,
                vertex2 = vertex2,
                successor2 = predecessor2,
            )
        } else {
            this.copy(
                vertex1 = successor1,
                successor1 = vertex1,
                vertex2 = successor2,
                successor2 = vertex2,
            )
        }
    }
}

enum class ApplicationType {
    APPLICABLE,
    MAYBE_APPLICABLE,
    NOT_APPLICABLE
}
