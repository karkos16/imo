package org.example.solvers.localSearch

import org.example.models.Instance
import org.example.models.Route
import org.example.models.Solution

class GreedyLocalSearchSolver(
    instance: Instance,
    neighborhoodType: NeighborhoodType,
    greedyStart: Boolean = false
) : LocalSearchSolver(instance, neighborhoodType, greedyStart) {

    override fun findBestSwap(route1: Route, route2: Route, neighborhoodType: NeighborhoodType): Solution {

        for (i in (0 until instance.size).shuffled()) {
            for (j in (i until instance.size).shuffled()) {
                if (i == j) continue

                val swapResult = processSwap(i, j, route1, route2, neighborhoodType)
                if (swapResult != null) {
                    return swapResult
                }
            }
        }
        return Solution(
            route1 = route1,
            route2 = route2
        )
    }
}