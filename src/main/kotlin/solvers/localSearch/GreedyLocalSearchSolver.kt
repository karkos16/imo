package org.example.solvers.localSearch

import org.example.models.Instance
import org.example.models.Route
import org.example.models.Solution
import org.example.utils.swapEdges
import org.example.utils.swapIndices

class GreedyLocalSearchSolver(
    instance: Instance,
    neighborhoodType: NeighborhoodType,
    greedyStart: Boolean = false
) : LocalSearchSolver(instance, neighborhoodType, greedyStart) {

    override fun findBestSwap(route1: Route, route2: Route, type: NeighborhoodType): Solution {
        val bestCost = route1.distance + route2.distance

        for (i in (0 until instance.size).shuffled()) {
            for (j in i until instance.size) {
                if (i != j) {
                    val concatenatedRoutes =
                        (route1.indices.dropLast(1) + route2.indices.dropLast(1)).toMutableList()

                    when (type) {
                        NeighborhoodType.VERTICES -> concatenatedRoutes.swapIndices(i, j)
                        NeighborhoodType.EDGES -> concatenatedRoutes.swapEdges(i, j)
                    }

                    val newRoute1 = concatenatedRoutes.subList(0, instance.size / 2) + concatenatedRoutes[0]
                    val newRoute2 =
                        concatenatedRoutes.subList(
                            instance.size / 2,
                            instance.size
                        ) + concatenatedRoutes[instance.size / 2]
                    val newRoute1Distance = calculateRouteLength(newRoute1)
                    val newRoute2Distance = calculateRouteLength(newRoute2)
                    val newCost = newRoute1Distance + newRoute2Distance

                    if (newCost < bestCost) {
                        return Solution(
                            Route(newRoute1.toMutableList(), newRoute1Distance),
                            Route(newRoute2.toMutableList(), newRoute2Distance)
                        )
                    }
                }
            }
        }
        return Solution(route1, route2)
    }
}