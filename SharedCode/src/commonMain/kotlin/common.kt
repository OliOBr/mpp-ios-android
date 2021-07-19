package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.minutes
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

expect fun platformName(): String

val client = HttpClient(){
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

// TODO: Date formatting isn't working with timezones
suspend fun makeGetRequestForJourneysData(originStationCRS: String, destStationCRS: String): JsonArray {
    println("calling makeGetRequestForJourneysData()")
    val dateFormat = DateFormat("yyyy-MM-ddTHH:mm:ss")
    val currentDateTime = DateTime.now() + TimeSpan(60000.0)
    var currentTime: String = currentDateTime.format(dateFormat)
    val suffix = ".000+00:00"
    currentTime += suffix
    try {
        val response: JsonObject = client.get("https://mobile-api-softwire2.lner.co.uk/v1/fares?") {
            parameter("originStation", originStationCRS)
            parameter("destinationStation", destStationCRS)
            parameter("noChanges", "false")
            parameter("numberOfAdults", 1)
            parameter("numberOfChildren", 0)
            parameter("journeyType", "single")
            parameter("outboundDateTime", currentTime)
//            parameter("outboundDateTime", "2021-07-24T15:15:15.000+01:00")
            parameter("outboundIsArriveBy", "false")
        }
        println(response)
        val trainsList: JsonElement? = response["outboundJourneys"]
        return trainsList!!.jsonArray
    } catch (e: Exception) {
        println("Error getting JourneysData from API.")
        println(e.message)
    }
    return JsonArray(listOf())
}

suspend fun makeGetRequestForStationsData(): JsonArray {
    println("calling makeGetRequestForStationsData()")
    try {
        val response: JsonObject = client.get("https://mobile-api-softwire2.lner.co.uk/v1/stations")
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

    val tickets: JsonElement? = json.jsonObject["tickets"]
    val ticketsArray: JsonArray? = tickets!!.jsonArray
    var offPeakSinglePriceFormatted: String
    offPeakSinglePriceFormatted = if(ticketsArray?.size == 0) {
        "No tickets available"
    } else{
        val offPeakSinglePriceInPounds: Int = ticketsArray?.get(0)?.jsonObject?.get("priceInPennies")
            .toString().toInt() / 100
        val offPeakSinglePenniesLeft: String = (ticketsArray?.get(0)?.jsonObject?.get("priceInPennies")
            .toString().toInt() %  100).toString().padStart(2,'0')
        "£$offPeakSinglePriceInPounds.$offPeakSinglePenniesLeft"
    }
    return Journey(originStation, destStation, departureTime, arrivalTime,
            status, offPeakSinglePriceFormatted)
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



