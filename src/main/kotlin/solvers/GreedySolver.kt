package org.example.solvers

import org.example.models.Instance

class GreedySolver(
    instance: Instance
) : Solver(instance) {

    override fun findNext(from: Int): Int? {
        return instance.distanceMatrix[from].withIndex()
            .filter { !used[it.index] }
            .minByOrNull { it.value }?.index
    }
}