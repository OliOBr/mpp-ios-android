package com.jetbrains.handson.mpp.mobile

import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay


expect fun platformName(): String


val client = HttpClient(){
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

fun createApplicationScreenMessage(): String {
    return "Kotlin Rocks on ${platformName()}"
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

suspend fun makeGetRequestForData(url: String):HttpResponse {
    val response: HttpResponse = client.get(url)
    println(response)
    return response
}




