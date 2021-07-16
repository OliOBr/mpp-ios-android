package com.jetbrains.handson.mpp.mobile

import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

expect fun platformName(): String

val client = HttpClient() {
    install(JsonFeature) {
        serializer = KotlinxSerializer(Json{
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}

fun getAPIURLWithSelectedStations(originStationCRS: String, destStationCRS: String): String {
    return "https://mobile-api-softwire2.lner.co.uk/v1/fares?originStation=$originStationCRS" +
            "&destinationStation=$destStationCRS&noChanges=false&numberOfAdults=2" +
            "&numberOfChildren=0&journeyType=single" +
            "&outboundDateTime=2021-07-24T14%3A30%3A00.000%2B01%3A00&outboundIsArriveBy=false"
}

suspend fun makeGetRequestForJourneysData(url: String): List<Journey> {
    println("calling makeGetRequestForJourneysData()")
    try {
        val response: JourneyCollection = client.get(url)
        return response.outboundJourneys
    } catch (e: Exception) {
        println("Error getting JourneysData from API.")
        println(e.message)
    }
    return listOf<Journey>()
}

suspend fun makeGetRequestForStationsData(url: String): List<Station> {
    println("calling makeGetRequestForStationsData()")
    try {
        val response: StationCollection = client.get(url)

        return response.stations
    } catch (e: Exception) {
        println("Error getting StationsData from API.")
        println(e)
    }
    return listOf()
}

@Serializable
data class StationCollection(val stations: List<Station>)

@Serializable
data class JourneyCollection(val outboundJourneys: List<Journey>)

@Serializable
data class Journey(val journeyId: String,
                   val departureTime: String,
                   val arrivalTime: String,
                   val originStation: Station,
                   val destinationStation: Station,
                   val isFastestJourney: Boolean,
                   val journeyDurationInMinutes: Int,
                   val primaryTrainOperator: Map<String, String>,
                   val status: String) {
    val formattedArrivalTime = Regex("(?<=T)(.{5})").find(arrivalTime)!!.value
    val formattedDepartureTime = Regex("(?<=T)(.{5})").find(departureTime)!!.value
}

@Serializable
data class Station(val name: String = "", val displayName: String = "", val nlc: String?, val crs: String?) {
    val stationName = if (!name.isNullOrEmpty()){name}else{displayName}

    override fun toString(): String {
        return stationName
    }
}

