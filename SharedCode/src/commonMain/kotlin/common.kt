package com.jetbrains.handson.mpp.mobile

import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

expect fun platformName(): String

val client = HttpClient(){
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

fun getAPIURLWithSelectedStations(originStationCRS: String, destStationCRS: String): String {
    return "https://mobile-api-softwire2.lner.co.uk/v1/fares?originStation=$originStationCRS" +
            "&destinationStation=$destStationCRS&noChanges=false&numberOfAdults=2" +
            "&numberOfChildren=0&journeyType=single" +
            "&outboundDateTime=2021-07-24T14%3A30%3A00.000%2B01%3A00&outboundIsArriveBy=false"
}

suspend fun makeGetRequestForJourneysData(url: String): JsonArray {
    println("calling makeGetRequestForJourneysData()")
    try {
        val response: JsonObject = client.get(url)
        println(response)
        val trainsList: JsonElement? = response["outboundJourneys"]
        return trainsList!!.jsonArray
    } catch (e: Exception) {
        println("Error getting JourneysData from API.")
        println(e.message)
    }
    return JsonArray(listOf())
}

suspend fun makeGetRequestForStationsData(url: String): JsonArray {
    println("calling makeGetRequestForStationsData()")
    try {
        val response: JsonObject = client.get(url)
        val stations: JsonElement? = response["stations"]
        println(response)
        return stations!!.jsonArray
    } catch (e: Exception) {
        println("Error getting StationsData from API.")
        println(e)
    }
    return JsonArray(listOf())
}

fun parseJSONElementToJourney(json: JsonElement): Journey {
    val originStation: String = json.jsonObject["originStation"]!!.jsonObject["displayName"]
            .toString().replace(Regex("^\"|\"$"), "")
    val destStation: String = json.jsonObject["destinationStation"]!!.jsonObject["displayName"]
            .toString().replace(Regex("^\"|\"$"), "")
    val departureTime: String = Regex("(?<=T)(.{5})").find(json.jsonObject["departureTime"]
        .toString())!!.value
    val arrivalTime: String = Regex("(?<=T)(.{5})").find(json.jsonObject["arrivalTime"]
            .toString())!!.value
    val status: String = json.jsonObject["status"]
            .toString().replace(Regex("^\"|\"$"), "")

    // TODO: Add ticket prices to Table
    val tickets: String = json.jsonObject["tickets"].toString()

    return Journey(originStation, destStation, departureTime, arrivalTime, status)
}

fun parseJSONElementToStation(json: JsonElement): Station {

    val stationName: String = json.jsonObject["name"]!!
            .toString().replace(Regex("^\"|\"$"), "")
    val crs: String = json.jsonObject["crs"]!!
            .toString().replace(Regex("^\"|\"$"), "")
    val nlc: String = json.jsonObject["nlc"]!!
            .toString().replace(Regex("^\"|\"$"), "")

    return Station(stationName, nlc, crs)
}



