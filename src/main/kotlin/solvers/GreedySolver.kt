package org.example.solvers

import org.example.models.Instance

class GreedyCycleSolver(instance: Instance) : Solver(instance) {
    override fun findNext(from: Int): Int? {
        val route = if (from in route1) route1 else route2
        val startPoint = if (from in route1) startPoint1 else startPoint2
        return instance.distanceMatrix[from].withIndex()
            .filter { !used[it.index] }
            .minByOrNull {
                route.add(it.index)
                route.add(startPoint)
                val routeLen = calculateRouteLength(route)
                route.remove(it.index)
                route.removeLast()

                routeLen
            }?.index
    }

}