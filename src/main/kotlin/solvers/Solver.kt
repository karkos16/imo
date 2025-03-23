package org.example.solvers

import org.example.models.Instance
import org.example.models.Route
import org.example.models.Solution
import java.util.*

abstract class Solver(
    val instance: Instance,
) {
    val used = BooleanArray(instance.size)
    internal var startPoint1 = Random().nextInt(instance.size)
    internal var startPoint2 =
        instance.distanceMatrix[startPoint1].withIndex().maxBy { it.value }.index

    internal val route1 = mutableListOf(startPoint1)
    internal val route2 = mutableListOf(startPoint2)

    fun reset() {
        used.fill(false)
        route1.clear()
        route2.clear()
        startPoint1 = Random().nextInt(instance.size)
        startPoint2 = instance.distanceMatrix[startPoint1].withIndex().maxBy { it.value }.index
        route1.add(startPoint1)
        route2.add(startPoint2)
    }

    open fun solve(): Solution {
        used[startPoint1] = true
        used[startPoint2] = true

        while (route1.size + route2.size != instance.size) {
            val nearest1 = findNext(route1.last())
            if (nearest1 != null) {
                route1.add(nearest1)
                used[nearest1] = true
            }

            val nearest2 = findNext(route2.last())
            if (nearest2 != null) {
                route2.add(nearest2)
                used[nearest2] = true
            }
        }

        route1.add(startPoint1)
        route2.add(startPoint2)

        return Solution(Route(route1, calculateRouteLength(route1)), Route(route2, calculateRouteLength(route2)))
    }

    internal fun calculateRouteLength(route: List<Int>): Int {
        return route.zipWithNext().sumOf { instance.distanceMatrix[it.first][it.second] }
    }

    open fun findNext(from: Int): Int? {return null}
}
