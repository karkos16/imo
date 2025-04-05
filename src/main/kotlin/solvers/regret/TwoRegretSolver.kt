package org.example.solvers.regret

import org.example.models.Instance
import org.example.models.Route
import org.example.models.Solution
import org.example.solvers.Solver

class TwoRegretSolver(instance: Instance) : Solver(instance) {

    data class BestInsertion(
        val insertionIndex: Int,
        val pointIndex: Int
    )

    override fun solve(): Solution {
        used[startPoint1] = true
        used[startPoint2] = true

        while (route1.size < instance.size / 2) {
            val bestInsertion1 = findBestInsertion(route1)
            if (bestInsertion1 != null) {
                route1.add(bestInsertion1.insertionIndex, bestInsertion1.pointIndex)
                used[bestInsertion1.pointIndex] = true
            }
        }

        while (route2.size < instance.size / 2) {
            val bestInsertion2 = findBestInsertion(route2)
            if (bestInsertion2 != null) {
                route2.add(bestInsertion2.insertionIndex, bestInsertion2.pointIndex)
                used[bestInsertion2.pointIndex] = true
            }
        }

        route1.add(startPoint1)
        route2.add(startPoint2)

        return Solution(Route(route1, calculateRouteLength(route1)), Route(route2, calculateRouteLength(route2)))
    }

    private fun findBestInsertion(route: MutableList<Int>): BestInsertion? {
        val startPoint = route[0]
        val candidates = used.withIndex().filter { !it.value }.map { it.index }
        if (candidates.isEmpty()) return null
        if (route.size < 2) {
            val minDist =
                instance.distanceMatrix[startPoint].withIndex().filter { it.index in candidates }.minBy { it.value }
            return BestInsertion(1, minDist.index)
        }

        var maxRegret = -1
        var candidateIndex = -1
        var candidateInsertionIndex = -1

        for (candidate in candidates) {
            val regretWithIndex = (1..route.size).asSequence().map { insertionIndex ->
                val routeLength = calculateRouteLength(route)
                route.add(insertionIndex, candidate)
                route.add(startPoint)
                val cost = calculateRouteLength(route) - routeLength
                route.remove(candidate)
                route.removeLast()
                Pair(insertionIndex, cost)
            }.sortedBy { it.second }.take(2).zipWithNext { a, b -> Pair(a.first, b.second - a.second) }.first()
            if (regretWithIndex.second > maxRegret) {
                candidateIndex = candidate
                candidateInsertionIndex = regretWithIndex.first
                maxRegret = regretWithIndex.second
            }
        }

        return BestInsertion(candidateInsertionIndex, candidateIndex)
    }
}