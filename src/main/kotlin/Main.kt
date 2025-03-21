package org.example

fun main() {
//    Read files from resources
    val content = InstanceReader::class.java.getResource("/kroA200.tsp")?.readText()!!
    val instance = InstanceReader.readKroABInstance(content)
    println(instance)

//    val x = doubleArrayOf(1357.0, 2650.0, 802.0, 1905.0, 1357.0) // Zamknięcie trasy
//    val y = doubleArrayOf(1905.0, 802.0, 2650.0, 1357.0, 1905.0)
//
//    val chart = XYChartBuilder()
//        .width(800)
//        .height(600)
//        .title("TSP Path")
//        .xAxisTitle("X")
//        .yAxisTitle("Y")
//        .build()
//
//    chart.addSeries("TSP Path", x, y)
}