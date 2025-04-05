package org.example.solvers.greedy

import org.example.models.Instance
import org.example.solvers.Solver

class GreedySolver(
    instance: Instance
) : Solver(instance) {

    override fun findNext(from: Int): Int? {
        return instance.distanceMatrix[from].withIndex()
            .filter { !used[it.index] }
            .minByOrNull { it.value }?.index
    }
}