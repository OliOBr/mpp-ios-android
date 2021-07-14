package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.Date
import kotlinx.serialization.*

@Serializable
data class Train(val originStation: String, val destinationStation: String, val departureTime: String, val arrivalTime: String)