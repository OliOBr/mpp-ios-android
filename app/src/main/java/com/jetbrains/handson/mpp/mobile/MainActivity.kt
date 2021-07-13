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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*




class MainActivity : AppCompatActivity(), ApplicationContract.View {

    lateinit var departureStationDropdown: Spinner
    lateinit var arrivalStationDropdown: Spinner


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
        val rvTrains: RecyclerView = findViewById(R.id.rvTrains)
        val trains = listOf(Train("Newton Abbot","Paddington", Date(2021,7,13,12,43),Date(2021,7,13,15,43)))
        val trainAdapter = TrainAdapter(trains)
        rvTrains.adapter = trainAdapter
        rvTrains.layoutManager = LinearLayoutManager(this)

    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }

    fun getData() {
        val departureStation=departureStationDropdown.selectedItem.toString()
        val arrivalStation = arrivalStationDropdown.selectedItem.toString()
        val url = getAPIURLWithSelectedStations(arrivalStation,departureStation)
        val presenter = ApplicationPresenter()
        println(presenter.getData(url))
    }



}
