package org.example.solvers.localSearch

import org.example.models.Instance
import org.example.models.Route
import org.example.models.Solution

class SteepestLocalSearchSolver(
    instance: Instance,
    neighborhoodType: NeighborhoodType,
    greedyStart: Boolean = false
) : LocalSearchSolver(instance, neighborhoodType, greedyStart) {

    override fun findBestSwap(route1: Route, route2: Route, neighborhoodType: NeighborhoodType): Solution {
        var bestRoute1 = route1
        var bestRoute2 = route2

        for (i in 0 until instance.size) {
            for (j in i until instance.size) {
                if (i == j) continue

                val swapResult = processSwap(i, j, bestRoute1, bestRoute2, neighborhoodType)
                if (swapResult != null) {
                    bestRoute1 = swapResult.route1
                    bestRoute2 = swapResult.route2
                }
            }
        }

        return Solution(bestRoute1, bestRoute2)
    }

}