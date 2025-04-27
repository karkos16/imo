package org.example.solvers.localSearch

import org.example.models.Instance
import org.example.models.MoveType
import org.example.models.Route
import org.example.models.Solution
import org.example.solvers.RandomSolver
import org.example.solvers.Solver
import org.example.solvers.greedy.GreedySolver
import org.example.utils.delta.calculateEdgeSwapDelta
import org.example.utils.delta.calculateVerticesSwapDelta
import org.example.utils.getMoveType
import org.example.utils.swapEdges
import org.example.utils.swapIndices
import kotlin.math.ceil

abstract class LocalSearchSolver(
    instance: Instance,
    private val neighborhoodType: NeighborhoodType,
    private val greedyStart: Boolean = false
) : Solver(instance) {
    override val chartDescription: String
        get() = "${this::class.simpleName}($neighborhoodType, greedyStart=$greedyStart)"

    override fun solve(): Solution {
        var bestSolution = if (greedyStart) GreedySolver(instance).solve() else RandomSolver(instance).solve()

        var improved = true
        while (improved) {
            improved = false
            val newSolution = findBestSwap(bestSolution.route1, bestSolution.route2, neighborhoodType)
            if (newSolution.route1.distance + newSolution.route2.distance < bestSolution.route1.distance + bestSolution.route2.distance) {
                bestSolution = newSolution
                improved = true
            }
        }

        return bestSolution
    }

    internal fun processSwap(
        i: Int,
        j: Int,
        route1: Route,
        route2: Route,
        neighborhoodType: NeighborhoodType
    ): Solution? {
        if (i == j) return null

        when (val moveType = getMoveType(i, j, instance.size)) {
            MoveType.INTER_ROUTES -> {
                val normalizedI = i % ceil(instance.size / 2.0).toInt()
                val normalizedJ = j % (instance.size / 2)

                if (calculateVerticesSwapDelta(
                        distanceMatrix = instance.distanceMatrix,
                        normalizedI,
                        normalizedJ,
                        route1.indices.dropLast(1),
                        route2.indices.dropLast(1)
                    ) < 0
                ) {
                    val newRoute1 = route1.indices.toMutableList()
                    val newRoute2 = route2.indices.toMutableList()
                    newRoute1[normalizedI] = route2.indices[normalizedJ]
                    newRoute2[normalizedJ] = route1.indices[normalizedI]

                    newRoute1[newRoute1.size - 1] = newRoute1[0]
                    newRoute2[newRoute2.size - 1] = newRoute2[0]
                    return Solution(
                        route1 = Route(newRoute1, calculateRouteLength(newRoute1)),
                        route2 = Route(newRoute2, calculateRouteLength(newRoute2))
                    )
                }
            }

            MoveType.INNER_ROUTE1, MoveType.INNER_ROUTE2 -> {
                val route = if (moveType == MoveType.INNER_ROUTE1) {
                    route1.indices
                } else {
                    route2.indices
                }
                val normalizedI = if (moveType == MoveType.INNER_ROUTE2) {
                    i % (route2.indices.size - 1) // -1 because of last vertex
                } else {
                    i % (route1.indices.size - 1) // -1 because of last vertex
                }
                val normalizedJ = if (moveType == MoveType.INNER_ROUTE2) {
                    j % (route2.indices.size - 1) // -1 because of last vertex
                } else {
                    j % (route1.indices.size - 1) // -1 because of last vertex
                }

                when (neighborhoodType) {
                    NeighborhoodType.VERTICES -> {
                        if (
                            calculateVerticesSwapDelta(
                                distanceMatrix = instance.distanceMatrix,
                                normalizedI,
                                normalizedJ,
                                route.dropLast(1),
                                null
                            ) < 0
                        ) {
                            val newRoute = route.toMutableList()
                            newRoute.swapIndices(normalizedI, normalizedJ)

                            newRoute[newRoute.size - 1] = newRoute[0]
                            return Solution(
                                route1 = if (moveType == MoveType.INNER_ROUTE1) {
                                    Route(newRoute, calculateRouteLength(newRoute))
                                } else {
                                    route1
                                },
                                route2 = if (moveType == MoveType.INNER_ROUTE2) {
                                    Route(newRoute, calculateRouteLength(newRoute))
                                } else {
                                    route2
                                }
                            )
                        }
                    }

                    NeighborhoodType.EDGES -> {
                        if (
                            calculateEdgeSwapDelta(
                                distanceMatrix = instance.distanceMatrix,
                                normalizedI,
                                normalizedJ,
                                route.dropLast(1)
                            ) < 0
                        ) {
                            val newRoute = route.toMutableList()
                            newRoute.swapEdges(normalizedI, normalizedJ)

                            newRoute[newRoute.size - 1] = newRoute[0]
                            return Solution(
                                route1 = if (moveType == MoveType.INNER_ROUTE1) {
                                    Route(newRoute, calculateRouteLength(newRoute))
                                } else {
                                    route1
                                },
                                route2 = if (moveType == MoveType.INNER_ROUTE2) {
                                    Route(newRoute, calculateRouteLength(newRoute))
                                } else {
                                    route2
                                }
                            )
                        }
                    }
                }
            }
        }
        return null
    }

    abstract fun findBestSwap(route1: Route, route2: Route, neighborhoodType: NeighborhoodType): Solution
}