package org.example

import org.example.models.City
import org.example.models.Instance
import org.example.models.Solution
import org.example.solvers.Solver
import org.example.solvers.greedy.GreedyCycleSolver
import org.example.solvers.greedy.GreedySolver
import org.example.solvers.localSearch.NeighborhoodType
import org.example.solvers.localSearch.SteepestLocalSearchSolver
import org.example.solvers.regret.TwoRegretSolver
import org.example.solvers.regret.WeightedTwoRegretSolver
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder

fun main() {
    val instanceNames = listOf("kroA200.tsp", "kroB200.tsp")
    for (instanceName in instanceNames) {
        val content = InstanceReader::class.java.getResource("/$instanceName")?.readText()!!
        val instance = InstanceReader.readKroABInstance(content)
        val cities = InstanceReader.getCities(content)
        localSearchSolversTest(instance, cities, instanceName)
    }
}

private fun runSolvers(cities: List<City>, solvers: List<Solver>, instanceName: String) {
    for (solver in solvers) {
        getBestTryIn100(solver, cities, instanceName)
    }
}

private fun localSearchSolversTest(instance: Instance, cities: List<City>, instanceName: String) {
    val solvers = listOf(
//        GreedyLocalSearchSolver(instance, NeighborhoodType.EDGES, greedyStart = true),
//        GreedyLocalSearchSolver(instance, NeighborhoodType.EDGES, greedyStart = false),
//        GreedyLocalSearchSolver(instance, NeighborhoodType.VERTICES, greedyStart = true),
//        GreedyLocalSearchSolver(instance, NeighborhoodType.VERTICES, greedyStart = false),
//        SteepestLocalSearchSolver(instance, NeighborhoodType.EDGES, greedyStart = true),
//        SteepestLocalSearchSolver(instance, NeighborhoodType.EDGES, greedyStart = false),
//        SteepestLocalSearchSolver(instance, NeighborhoodType.VERTICES, greedyStart = true),
        SteepestLocalSearchSolver(instance, NeighborhoodType.VERTICES, greedyStart = false)
    )
    runSolvers(cities, solvers, instanceName)
}

private fun greedySolversTest(instance: Instance, cities: List<City>, instanceName: String) {
    val solvers = listOf(
        GreedySolver(instance),
        GreedyCycleSolver(instance),
        TwoRegretSolver(instance),
        WeightedTwoRegretSolver(instance)
    )
    runSolvers(cities, solvers, instanceName)
}

fun getBestTryIn100(solver: Solver, cities: List<City>, instanceName: String) {
    val results = mutableListOf<Int>()
    val times = mutableListOf<Long>()
    var bestSolution: Solution? = null
    var bestLength = Int.MAX_VALUE
    var worstLength = Int.MIN_VALUE
    var worstTime = Long.MIN_VALUE
    var bestTime = Long.MAX_VALUE
    for (n in 1..100) {
        solver.reset()
        val startTime = System.currentTimeMillis()
        val solution = solver.solve()
        val endTime = System.currentTimeMillis()
        val time = endTime - startTime
        times.add(time)
        if (time > worstTime) {
            worstTime = time
        }
        if (time < bestTime) {
            bestTime = time
        }

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
    val averageTime = times.average()

    println("==========================")
    println("Solver: ${solver.chartDescription}, Instance: $instanceName")
    println("Average length: $averageLength")
    println("Best length: $bestLength")
    println("Worst length: $worstLength")
    println("Average time: $averageTime ms")
    println("Best time: $bestTime ms")
    println("Worst time: $worstTime ms")
    println("==========================")

    // Visualize the best solution
    if (bestSolution != null) {

        val x1 = bestSolution.route1.indices.map { cities[it] }.map { it.coordinates.x.toDouble() }.toDoubleArray()
        val y1 = bestSolution.route1.indices.map { cities[it] }.map { it.coordinates.y.toDouble() }.toDoubleArray()
        val x2 = bestSolution.route2.indices.map { cities[it] }.map { it.coordinates.x.toDouble() }.toDoubleArray()
        val y2 = bestSolution.route2.indices.map { cities[it] }.map { it.coordinates.y.toDouble() }.toDoubleArray()

        val chart = XYChartBuilder()
            .width(800)
            .height(600)
            .title("TSP Path - $instanceName - ${solver.chartDescription}")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build()

        chart.addSeries("Route 1", x1, y1)
        chart.addSeries("Route 2", x2, y2)
        BitmapEncoder.saveBitmap(chart, "${instanceName}_${solver.chartDescription}", BitmapEncoder.BitmapFormat.PNG)
        SwingWrapper(chart).displayChart()
    }
}