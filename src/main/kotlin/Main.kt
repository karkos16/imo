package org.example

import org.example.solvers.GreedyCycleSolver
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder

fun main() {
//    Read files from resources
    val content = InstanceReader::class.java.getResource("/kroA200.tsp")?.readText()!!
    val instance = InstanceReader.readKroABInstance(content)
    val solver = GreedyCycleSolver(instance)

    val solution = solver.solve()
    val cities = InstanceReader.getCities(content)

    val x1 = solution.route1.indices.map { cities[it] }.map { it.coordinates.x.toDouble() }.toDoubleArray()
    val y1 = solution.route1.indices.map { cities[it] }.map { it.coordinates.y.toDouble() }.toDoubleArray()
    val x2 = solution.route2.indices.map { cities[it] }.map { it.coordinates.x.toDouble() }.toDoubleArray()
    val y2 = solution.route2.indices.map { cities[it] }.map { it.coordinates.y.toDouble() }.toDoubleArray()

    val chart = XYChartBuilder()
        .width(800)
        .height(600)
        .title("TSP Path")
        .xAxisTitle("X")
        .yAxisTitle("Y")
        .build()

    chart.addSeries("Route 1", x1, y1)
    chart.addSeries("Route 2", x2, y2)

    SwingWrapper(chart).displayChart()
}