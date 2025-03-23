package org.example.models

data class Instance(
    val name: String,
    val size: Int,
    val distanceMatrix: List<List<Int>>,
)
