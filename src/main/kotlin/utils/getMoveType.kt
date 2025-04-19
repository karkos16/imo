package org.example.utils

import org.example.models.MoveType
import kotlin.math.ceil

fun getMoveType(
    from: Int,
    to: Int,
    instanceSize: Int
): MoveType {
    return if (from < ceil(instanceSize / 2.0) && to < ceil(instanceSize / 2.0)) {
        MoveType.INNER_ROUTE1
    } else if (from >= ceil(instanceSize / 2.0) && to >= ceil(instanceSize / 2.0)) {
        MoveType.INNER_ROUTE2
    } else MoveType.INTER_ROUTES
}