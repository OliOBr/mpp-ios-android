package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity(), ApplicationContract.View {

    lateinit var departureStationDropdown: Spinner
    lateinit var arrivalStationDropdown: Spinner

    val client = HttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        departureStationDropdown = findViewById(R.id.spinner)
        arrivalStationDropdown = findViewById(R.id.spinner2)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.stations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            departureStationDropdown.adapter = adapter
            arrivalStationDropdown.adapter = adapter
        }

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{getData()}
    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }

    fun getData() = runBlocking { // this: CoroutineScope
        launch { // launch a new coroutine and continue
            makeGetRequestForData()
        }
    }

    suspend fun makeGetRequestForData() {
        val departureStation=departureStationDropdown.selectedItem.toString()
        val arrivalStation = arrivalStationDropdown.selectedItem.toString()
        val url = getAPIURLWithSelectedStations(arrivalStation,departureStation)
        val response: HttpResponse = client.get(url)
        println(response);
    }

}
