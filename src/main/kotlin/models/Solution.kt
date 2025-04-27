package org.example.models

import org.example.solvers.localSearchMoveRating.ApplicationType
import org.example.solvers.localSearchMoveRating.Move

data class Solution(
    val route1: Route,
    val route2: Route
) {
    fun isApplicable(move: Move): ApplicationType {
        return when (move.moveType) {
            MoveType.INNER_ROUTE1, MoveType.INNER_ROUTE2 -> {
                val route = if (move.moveType == MoveType.INNER_ROUTE1) route1 else route2

                val edge1Existence = route.checkEdgeExistence(move.vertex1, move.successor1)
                val edge2Existence = route.checkEdgeExistence(move.vertex2, move.successor2)

                if (move.successor1 == move.vertex2 || move.successor2 == move.vertex1) {
                    return ApplicationType.NOT_APPLICABLE
                }

                if (edge1Existence == EdgeExistence.NOT_EXIST || edge2Existence == EdgeExistence.NOT_EXIST) {
                    return ApplicationType.NOT_APPLICABLE
                } else if (edge1Existence == EdgeExistence.REVERSED || edge2Existence == EdgeExistence.REVERSED) {
                    return ApplicationType.MAYBE_APPLICABLE
                } else if (edge1Existence == EdgeExistence.EXIST && edge2Existence == EdgeExistence.EXIST) {
                    return ApplicationType.APPLICABLE
                } else {
                    return ApplicationType.NOT_APPLICABLE
                }
            }

            MoveType.INTER_ROUTES -> {
                val vertex1Index = route1.indices.indexOf(move.vertex1)
                val vertex2Index = route2.indices.indexOf(move.vertex2)
                if (vertex1Index == -1 || vertex2Index == -1) {
                    return ApplicationType.NOT_APPLICABLE
                }
                val currentSuccessor1 = route1.indices.dropLast(1).getOrElse(vertex1Index + 1) { route1.indices[0] }
                val currentSuccessor2 = route2.indices.dropLast(1).getOrElse(vertex2Index + 1) { route2.indices[0] }
                val currentPredecessor1 =
                    route1.indices.dropLast(1).getOrElse(vertex1Index - 1) { route1.indices.dropLast(1).last() }
                val currentPredecessor2 =
                    route2.indices.dropLast(1).getOrElse(vertex2Index - 1) { route2.indices.dropLast(1).last() }

                if ((currentSuccessor1 == move.successor1 &&
                    currentSuccessor2 == move.successor2 &&
                    currentPredecessor1 == move.predecessor1 &&
                    currentPredecessor2 == move.predecessor2) || (
                        currentSuccessor1 == move.predecessor1 &&
                        currentSuccessor2 == move.predecessor2 &&
                        currentPredecessor1 == move.successor1 &&
                        currentPredecessor2 == move.successor2
                            )
                ) {
                    return ApplicationType.APPLICABLE
                } else {
                    return ApplicationType.NOT_APPLICABLE
                }
            }
        }
    }
}