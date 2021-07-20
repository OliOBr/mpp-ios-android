package com.jetbrains.handson.mpp.mobile

import kotlinx.serialization.*
import kotlin.math.*

@Serializable
data class Station(val stationName: String, val nlc: String, val crs: String, val longitude: Double?, val latitude: Double?, var distanceFromLocation: Double? = null) {
    override fun toString(): String {
        return stationName
    }
    //TODO: London terminals have null latitude and longitude
    fun getDistanceFromLocation(locationLongitude: Double, locationLatitude: Double): Double? {
        return if(longitude != null  && latitude != null) {
            val theta: Double = longitude - locationLongitude
            var dist =
                sin(latitude * PI / 180) * sin(locationLatitude * PI / 180) + cos(latitude * PI / 180) * cos(
                    locationLatitude * PI / 180
                ) * cos(theta * PI / 180)
            dist = acos(dist)
            dist = dist * 180 / PI
            dist *= 60 * 1.1515
            round(dist*10)/10
        } else {
            null
        }
    }
}