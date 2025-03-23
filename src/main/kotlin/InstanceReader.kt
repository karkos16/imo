package org.example

import org.example.models.City
import org.example.models.Coordinates
import org.example.models.Instance

object InstanceReader {
    fun readKroABInstance(content: String): Instance {
        val lines = content.split("\n")
        val cities = getCities(content)

        val distances = List(cities.size) {MutableList(cities.size) { 0 } }

        for (i in cities) {
            for (j in cities) {
                distances[i.id - 1][j.id - 1] = i.coordinates.distanceTo(j.coordinates)
            }
        }

        return Instance(lines.find { it.startsWith("NAME") }!!.substringAfter(": "), cities.size, distances)
    }

    fun getCities(content: String): List<City> {
        val lines = content.split("\n")
        val cities = mutableListOf<City>()
        lines.forEach { line ->
            if (line.matches("\\d+\\s+\\d+\\s+\\d+".toRegex())) {
                val (id, x, y) = line.split("\\s+".toRegex()).map { it.toInt() }
                cities.add(City(id, Coordinates(x, y)))
            }
        }
        return cities
    }
}