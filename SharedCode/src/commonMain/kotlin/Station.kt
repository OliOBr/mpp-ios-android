package com.jetbrains.handson.mpp.mobile

import kotlinx.serialization.*

@Serializable
data class Station(val stationName: String, val nlc: String, val crs: String) {
    override fun toString(): String {
        return stationName
    }
}