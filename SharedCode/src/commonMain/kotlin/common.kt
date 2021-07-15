package com.jetbrains.handson.mpp.mobile

import io.ktor.client.*
import io.ktor.client.call.receive
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.cio.Response
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.StructureKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.parse


expect fun platformName(): String


val client = HttpClient(){
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

fun getAPIURLWithSelectedStations(arrivalStation: String, departureStation: String): String {
    val arrivalStationCRS = stationStringToCRS(arrivalStation)
    val departureStationCRS = stationStringToCRS(departureStation)
    return "https://mobile-api-softwire2.lner.co.uk/v1/fares?originStation=$departureStationCRS" +
            "&destinationStation=$arrivalStationCRS&noChanges=false&numberOfAdults=2" +
            "&numberOfChildren=0&journeyType=single" +
            "&outboundDateTime=2021-07-24T14%3A30%3A00.000%2B01%3A00&outboundIsArriveBy=false"
}

fun stationStringToCRS(station: String): String {
    return when(station){
        "Newton Abbot" -> "NTA"
        "Paddington" -> "PAD"
        "Durham" -> "DHM"
        "Cambridge" -> "CBG"
        "Waterloo" -> "WAT"
        else -> station
    }
}

suspend fun makeGetRequestForJourneysData(url: String): JsonArray {
    try {
        val response: JsonObject = client.get(url)
        println(response)
        val trainsList: JsonElement? = response["outboundJourneys"]
        return trainsList!!.jsonArray
    } catch (e: Exception) {
        println("Error getting JourneysData from API.")
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

    return Journey(originStation, destStation, departureTime, arrivalTime, status)
}



