package com.jetbrains.handson.mpp.mobile

expect fun platformName(): String

fun createApplicationScreenMessage(): String {
    return "Kotlin Rocks on ${platformName()}"
}

fun getAPIURLWithSelectedStations(arrivalStation: String, departureStation: String): String {
    val arrivalStationCRS = stationStringToCRS(arrivalStation)
    val departureStationCRS = stationStringToCRS(departureStation)
    return "https://mobile-api-softwire2.lner.co.uk/v1/fares?originStation=$departureStationCRS&destinationStation=$arrivalStationCRS&noChanges=false&numberOfAdults=2&numberOfChildren=0&journeyType=single&outboundDateTime=2021-07-24T14%3A30%3A00.000%2B01%3A00&outboundIsArriveBy=false"
}

fun stationStringToCRS(station: String): String {
    return when(station){
        "Newton Abbot" -> "NTA"
        "Paddington" -> "PAD"
        "Durham" -> "DHM"
        "Cambridge" -> "CBG"
        "Waterloo" -> "WAT"
        else -> throw error("Station name not on predetermined list")
    }
}

