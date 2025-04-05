package org.example

import org.example.models.Solution
import org.example.solvers.greedy.GreedyCycleSolver
import org.example.solvers.greedy.GreedySolver
import org.example.solvers.localSearch.GreedyLocalSearchSolver
import org.example.solvers.localSearch.NeighborhoodType
import org.example.solvers.regret.TwoRegretSolver
import org.example.solvers.regret.WeightedTwoRegretSolver
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder

fun main() {
    val content = InstanceReader::class.java.getResource("/kroA200.tsp")?.readText()!!
    val instance = InstanceReader.readKroABInstance(content)
    val solvers = listOf(
        GreedyLocalSearchSolver(instance, NeighborhoodType.EDGES, greedyStart = true),
        GreedyLocalSearchSolver(instance, NeighborhoodType.EDGES, greedyStart = false),
    )
    for (solver in solvers) {
        solver.reset()
        val solution = solver.solve()

        val cities = InstanceReader.getCities(content)

        val x1 = solution.route1.indices.map { cities[it] }.map { it.coordinates.x.toDouble() }.toDoubleArray()
        val y1 = solution.route1.indices.map { cities[it] }.map { it.coordinates.y.toDouble() }.toDoubleArray()
        val x2 = solution.route2.indices.map { cities[it] }.map { it.coordinates.x.toDouble() }.toDoubleArray()
        val y2 = solution.route2.indices.map { cities[it] }.map { it.coordinates.y.toDouble() }.toDoubleArray()

        val chart = XYChartBuilder()
            .width(800)
            .height(600)
            .title("TSP Path - kroA200.tsp - ${solver::class.simpleName}")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build()

        chart.addSeries("Route 1", x1, y1)
        chart.addSeries("Route 2", x2, y2)

        SwingWrapper(chart).displayChart()
    }
}

private fun greedySolversTest() {
    //    Read files from resources
    val content = InstanceReader::class.java.getResource("/kroA200.tsp")?.readText()!!
    val instance = InstanceReader.readKroABInstance(content)
    val solvers = arrayOf(
        GreedySolver(instance),
        GreedyCycleSolver(instance),
        TwoRegretSolver(instance),
        WeightedTwoRegretSolver(instance)
    )
    for (solver in solvers) {
        val results = mutableListOf<Int>()
        var bestSolution: Solution? = null
        var bestLength = Int.MAX_VALUE
        var worstLength = Int.MIN_VALUE
        for (n in 1..100) {
            solver.reset()
            val solution = solver.solve()
            val totalLength = solution.route1.distance + solution.route2.distance
            results.add(totalLength)

            if (totalLength < bestLength) {
                bestLength = totalLength
                bestSolution = solution
            }
            if (totalLength > worstLength) {
                worstLength = totalLength
            }
        }

        val averageLength = results.average()

        println("Solver: ${solver::class.simpleName}")
        println("Average length: $averageLength")
        println("Best length: $bestLength")
        println("Worst length: $worstLength")

        // Visualize the best solution
        if (bestSolution != null) {
            val cities = InstanceReader.getCities(content)

            val x1 = bestSolution.route1.indices.map { cities[it] }.map { it.coordinates.x.toDouble() }.toDoubleArray()
            val y1 = bestSolution.route1.indices.map { cities[it] }.map { it.coordinates.y.toDouble() }.toDoubleArray()
            val x2 = bestSolution.route2.indices.map { cities[it] }.map { it.coordinates.x.toDouble() }.toDoubleArray()
            val y2 = bestSolution.route2.indices.map { cities[it] }.map { it.coordinates.y.toDouble() }.toDoubleArray()

            val chart = XYChartBuilder()
                .width(800)
                .height(600)
                .title("TSP Path - kroA200.tsp - ${solver::class.simpleName}")
                .xAxisTitle("X")
                .yAxisTitle("Y")
                .build()

            chart.addSeries("Route 1", x1, y1)
            chart.addSeries("Route 2", x2, y2)

            SwingWrapper(chart).displayChart()
        }
    }

}