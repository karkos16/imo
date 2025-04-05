package org.example.solvers

import org.example.models.Instance
import org.example.models.Route
import org.example.models.Solution

class RandomSolver(instance: Instance) : Solver (instance) {
    override fun solve(): Solution {
        val route1 = mutableListOf<Int>()
        val route2 = mutableListOf<Int>()
        val used = BooleanArray(instance.size) { false }
        var currentCity = (0 until instance.size).random()
        route1.add(currentCity)
        used[currentCity] = true

        while (route1.size < instance.size/2) {
            currentCity = (0 until instance.size).filter { !used[it] }.random()
            route1.add(currentCity)
            used[currentCity] = true
        }
        while (route2.size < instance.size/2) {
            currentCity = (0 until instance.size).filter { !used[it] }.random()
            route2.add(currentCity)
            used[currentCity] = true
        }

        route1.add(route1[0])
        route2.add(route2[0])

        return Solution(Route(route1, calculateRouteLength(route1)), Route(route2, calculateRouteLength(route2)))
    }
}