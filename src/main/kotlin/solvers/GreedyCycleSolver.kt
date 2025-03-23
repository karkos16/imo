package org.example.solvers

import org.example.models.Instance

class GreedyCycleSolver(instance: Instance) : Solver(instance) {
    override fun findNext(from: Int): Int? {
        return instance.distanceMatrix[from].withIndex()
            .filter { !used[it.index] }
            .minByOrNull {
                val routeLen: Int
                if (from in route1) {
                    route1.add(it.index)
                    route1.add(startPoint1)
                    routeLen = calculateRouteLength(route1)
                    route1.remove(it.index)
                    route1.remove(startPoint1)
                } else {
                    route2.add(it.index)
                    route2.add(startPoint2)
                    routeLen = calculateRouteLength(route2)
                    route2.remove(it.index)
                    route2.remove(startPoint2)
                }
                routeLen
            }?.index
    }

}