package org.example.solvers.localSearchMoveRating

import org.example.models.Instance
import org.example.models.MoveType
import org.example.models.Route
import org.example.models.Solution
import org.example.solvers.RandomSolver
import org.example.solvers.localSearch.LocalSearchSolver
import org.example.solvers.localSearch.NeighborhoodType
import org.example.utils.delta.calculateEdgeSwapDelta
import org.example.utils.delta.calculateVerticesSwapDelta
import org.example.utils.getMoveType
import org.example.utils.swapEdges
import kotlin.math.ceil

class SteepestHistoryLocalSearchSolver(
    instance: Instance,
    neighborhoodType: NeighborhoodType = NeighborhoodType.EDGES,
    greedyStart: Boolean = false
) : LocalSearchSolver(instance, neighborhoodType, greedyStart) {
    override val chartDescription: String
        get() = "Lokalne przeszukiwanie z ruchami z poprzednich iteracji"

    private fun getMove(i: Int, j: Int, solution: Solution, calculateDelta: Boolean = false): Move {
        val moveType = getMoveType(i, j, instance.size)
        val normalizedI = i % ceil(instance.size / 2.0).toInt()
        val normalizedJ = j % (instance.size / 2)

        return when (moveType) {
            MoveType.INNER_ROUTE1 -> Move(
                moveType = moveType,
                vertex1 = solution.route1.indices[normalizedI],
                successor1 = solution.route1.indices.dropLast(1).getOrElse(normalizedI + 1) { solution.route1.indices[0] },
                predecessor1 = null,
                vertex2 = solution.route1.indices[normalizedJ],
                successor2 = solution.route1.indices.dropLast(1).getOrElse(normalizedJ + 1) { solution.route1.indices[0] },
                predecessor2 = null,
                delta = if (calculateDelta) calculateEdgeSwapDelta(
                    instance.distanceMatrix,
                    normalizedI,
                    normalizedJ,
                    solution.route1.indices.dropLast(1)
                ) else 0
            )

            MoveType.INNER_ROUTE2 -> Move(
                moveType = moveType,
                vertex1 = solution.route2.indices[normalizedI],
                successor1 = solution.route2.indices.dropLast(1).getOrElse(normalizedI + 1) { solution.route2.indices[0] },
                predecessor1 = null,
                vertex2 = solution.route2.indices[normalizedJ],
                successor2 = solution.route2.indices.dropLast(1).getOrElse(normalizedJ + 1) { solution.route2.indices[0] },
                predecessor2 = null,
                delta = if (calculateDelta) calculateEdgeSwapDelta(
                    instance.distanceMatrix,
                    normalizedI,
                    normalizedJ,
                    solution.route2.indices.dropLast(1)
                ) else 0
            )

            MoveType.INTER_ROUTES -> Move(
                moveType = moveType,
                vertex1 = solution.route1.indices[normalizedI],
                successor1 = solution.route1.indices.dropLast(1).getOrElse(normalizedI + 1) { solution.route1.indices[0] },
                predecessor1 = solution.route1.indices.dropLast(1).getOrElse(normalizedI - 1) {solution.route1.indices[solution.route1.indices.size - 2] },
                vertex2 = solution.route2.indices[normalizedJ],
                successor2 = solution.route2.indices.dropLast(1).getOrElse(normalizedJ + 1) { solution.route2.indices[0] },
                predecessor2 = solution.route2.indices.dropLast(1).getOrElse(normalizedJ - 1) {solution.route2.indices[solution.route2.indices.size - 2] },
                delta = if (calculateDelta) calculateVerticesSwapDelta(
                    instance.distanceMatrix,
                    normalizedI,
                    normalizedJ,
                    solution.route1.indices.dropLast(1),
                    solution.route2.indices.dropLast(1)
                ) else 0,
            )
        }
    }

    override fun solve(): Solution {
        var solution = RandomSolver(instance).solve()
        val movesSet = HashSet<Move>()
        var applicableMoveFound = true

        while (applicableMoveFound) {
            applicableMoveFound = false
            for (i in 0 until instance.size) {
                for (j in i until instance.size) {
                    if (i == j) continue
                    if (i + 1 == j) continue
                    var potentialMove = getMove(i, j, solution)

                    if (!movesSet.contains(potentialMove)) {
                        potentialMove = getMove(i, j, solution, calculateDelta = true)

                        if (potentialMove.delta < 0) {
                            movesSet.add(potentialMove)
                        }
                    }
                    var reversedEdgesMove = potentialMove.reverseEdge()

                    if (!movesSet.contains(reversedEdgesMove)) {
                        if (reversedEdgesMove.delta == 0) {
                            reversedEdgesMove = getMove(i, j, solution, calculateDelta = true).reverseEdge()
                        }
                        if (reversedEdgesMove.delta < 0) {
                            movesSet.add(reversedEdgesMove)
                        }
                    }
                }
            }

            for (move in movesSet.toSortedSet(compareBy { it.delta })) {
                when (move.moveType) {
                    MoveType.INNER_ROUTE1, MoveType.INNER_ROUTE2 -> {
                        when (solution.isApplicable(move)) {
                            ApplicationType.APPLICABLE -> {
                                applicableMoveFound = true
                                val route = if (move.moveType == MoveType.INNER_ROUTE1) {
                                    solution.route1
                                } else {
                                    solution.route2
                                }

                                val vertex1Index = route.indices.indexOf(move.vertex1)
                                val vertex2Index = route.indices.indexOf(move.vertex2)

                                val newRoute = route.indices.dropLast(1).toMutableList()
                                newRoute.swapEdges(vertex1Index, vertex2Index)
                                newRoute.add(newRoute[0])

                                solution = Solution(
                                    route1 = if (move.moveType == MoveType.INNER_ROUTE1) {
                                        Route(newRoute, calculateRouteLength(newRoute))
                                    } else {
                                        solution.route1
                                    },
                                    route2 = if (move.moveType == MoveType.INNER_ROUTE2) {
                                        Route(newRoute, calculateRouteLength(newRoute))
                                    } else {
                                        solution.route2
                                    }
                                )
                                movesSet.remove(move)
                                break
                            }

                            ApplicationType.MAYBE_APPLICABLE -> {
                                continue
                            }

                            ApplicationType.NOT_APPLICABLE -> {
                                movesSet.remove(move)
                                continue
                            }
                        }
                    }

                    MoveType.INTER_ROUTES -> {
                        if (solution.isApplicable(move) == ApplicationType.APPLICABLE) {
                            applicableMoveFound = true
                            val vertex1Index = solution.route1.indices.indexOf(move.vertex1)
                            val vertex2Index = solution.route2.indices.indexOf(move.vertex2)

                            val newRoute1 = solution.route1.indices.toMutableList()
                            val newRoute2 = solution.route2.indices.toMutableList()

                            newRoute1[vertex1Index] = move.vertex2
                            newRoute2[vertex2Index] = move.vertex1
                            newRoute1[newRoute1.size - 1] = newRoute1[0]
                            newRoute2[newRoute2.size - 1] = newRoute2[0]

                            solution = Solution(
                                route1 = Route(newRoute1, calculateRouteLength(newRoute1)),
                                route2 = Route(newRoute2, calculateRouteLength(newRoute2))
                            )
                            movesSet.remove(move)
                            break
                        } else {
                            movesSet.remove(move)
                            continue
                        }
                    }
                }
            }
        }
        return solution
    }

    override fun findBestSwap(route1: Route, route2: Route, neighborhoodType: NeighborhoodType): Solution {
        TODO("Not yet implemented")
    }


}