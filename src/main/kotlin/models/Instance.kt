package org.example.models

data class Instance(
    val name: String,
    val dimension: Int,
    val distanceMatrix: List<List<Int>>,
)
