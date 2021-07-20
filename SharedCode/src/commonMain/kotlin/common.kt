package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

expect fun platformName(): String

val client = HttpClient(){
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun makeGetRequestForJourneysData(originStationCRS: String, destStationCRS: String,
                                          numberAdults: String, numberChildren: String,
                                          noChanges: String): JsonArray {
    println("calling makeGetRequestForJourneysData()")
    println("numberAdults is $numberAdults")
    val dateFormat = DateFormat("yyyy-MM-ddTHH:mm:ss")
    val currentDateTime = DateTime.now() + TimeSpan(60000.0)
    var currentTime: String = currentDateTime.format(dateFormat)
    val suffix = ".000+00:00"
    currentTime += suffix
    try {
        val response: JsonObject = client.get("https://mobile-api-softwire2.lner.co.uk/v1/fares?") {
            parameter("originStation", originStationCRS)
            parameter("destinationStation", destStationCRS)
            parameter("noChanges", noChanges)
            parameter("numberOfAdults", numberAdults)
            parameter("numberOfChildren", numberChildren)
            parameter("journeyType", "single")
            parameter("outboundDateTime", currentTime)
            parameter("outboundIsArriveBy", "false")
        }
            //println(response)
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
        //println(response)
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
    val nextDay: Boolean = (json.jsonObject["arrivalTime"].toString()[10].toInt()-json.jsonObject["departureTime"].toString()[10].toInt()) == 1
    val status: String = json.jsonObject["status"]
            .toString().replace(Regex("^\"|\"$"), "")

    val tickets: JsonElement? = json.jsonObject["tickets"]
    val ticketsArray: JsonArray? = tickets!!.jsonArray
    var offPeakSinglePriceFormatted: String
    offPeakSinglePriceFormatted = if(ticketsArray?.size == 0 || ticketsArray == null) {
        "No tickets available"
    } else{
        var cheapestTicketPrice: Int = ticketsArray?.get(0)?.jsonObject?.get("priceInPennies")
            .toString().toInt()
        for (ticket in ticketsArray) {
            if(cheapestTicketPrice >= ticket?.jsonObject?.get("priceInPennies")
                    .toString().toInt()){
                cheapestTicketPrice = ticket?.jsonObject?.get("priceInPennies")
                    .toString().toInt()
            }
        }

        val offPeakSinglePriceInPounds: Int = cheapestTicketPrice / 100
        val offPeakSinglePenniesLeft: String = (cheapestTicketPrice %  100).toString().padStart(2,'0')
        "£$offPeakSinglePriceInPounds.$offPeakSinglePenniesLeft"
    }
    return Journey(originStation, destStation, departureTime, arrivalTime,
            status, offPeakSinglePriceFormatted, nextDay)
}

fun parseJSONElementToStation(json: JsonElement): Station {
    val stationName: String = json.jsonObject["name"]!!
            .toString().replace(Regex("^\"|\"$"), "")
    val crs: String = json.jsonObject["crs"]!!
            .toString().replace(Regex("^\"|\"$"), "")
    val nlc: String = json.jsonObject["nlc"]!!
            .toString().replace(Regex("^\"|\"$"), "")
    val longitude: Double? = json.jsonObject["longitude"].toString().replace(Regex("^\"|\"$"), "").toDoubleOrNull()
    val latitude: Double? = json.jsonObject["latitude"].toString().replace(Regex("^\"|\"$"), "").toDoubleOrNull()
    return Station(stationName, nlc, crs, longitude, latitude)
}





