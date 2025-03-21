package org.example.models

import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class Coordinates(
    val x: Int,
    val y: Int
) {
    fun distanceTo(other: Coordinates): Int {
        return sqrt((x - other.x).toDouble().pow(2) + (y - other.y).toDouble().pow(2)).roundToInt()
    }
}
