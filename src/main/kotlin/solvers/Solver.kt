package org.example.solvers

import org.example.models.Instance
import org.example.models.Solution

interface Solver {
    fun solve(instance: Instance): Solution
}