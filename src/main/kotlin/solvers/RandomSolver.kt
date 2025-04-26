package org.example.solvers

import org.example.models.Instance

class RandomSolver(instance: Instance) : Solver (instance) {
    override fun findNext(from: Int): Int? {
        return instance.distanceMatrix[from].withIndex()
            .filter { !used[it.index] }
            .randomOrNull()?.index
    }
}