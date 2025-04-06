package org.example.solvers.localSearch

import org.example.models.Instance
import org.example.models.Route
import org.example.models.Solution
import org.example.solvers.RandomSolver
import org.example.solvers.Solver
import org.example.solvers.greedy.GreedySolver

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

    abstract fun findBestSwap(route1: Route, route2: Route, type: NeighborhoodType): Solution
}