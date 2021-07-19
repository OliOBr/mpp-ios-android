package com.jetbrains.handson.mpp.mobile

import kotlinx.serialization.*

@Serializable
data class Journey(val originStation: String, val destinationStation: String, val departureTime: String, val arrivalTime: String, val status: String, val ticketPriceInPounds: String, val nextDay: Boolean)