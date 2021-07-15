package com.jetbrains.handson.mpp.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    lateinit var departureStationText: EditText
    lateinit var arrivalStationText: EditText

    lateinit var presenter: ApplicationPresenter

    private val departureStationStart = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            departureStationText.setText(result.data!!.getStringExtra("Result"))
        }
    }

    private val arrivalStationStart = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            arrivalStationText.setText(result.data!!.getStringExtra("Result"))
        }
    }

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        departureStationText = findViewById(R.id.departureStationText)
        arrivalStationText = findViewById(R.id.arrivalStationText)
        departureStationText.setOnClickListener{
            departureStationStart.launch(Intent(this,SearchStationsActivity::class.java))
        }
        arrivalStationText.setOnClickListener{
            arrivalStationStart.launch(Intent(this,SearchStationsActivity::class.java))
        }

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{getAndDisplayJourneysData(this)}
    }

    // TODO: to catch exception for when originDest same as targetDest, or app crashes.
    override fun getAndDisplayJourneysData(view: ApplicationContract.View) {
        val departureStation = departureStationText.text.toString()
        val arrivalStation = arrivalStationText.text.toString()
        val url = getAPIURLWithSelectedStations(arrivalStation, departureStation)
        presenter.getAndDisplayJourneysData(view, url)
    }

    override fun displayJourneysInRecyclerView(newTrains: List<Train>) {
        val rvTrains: RecyclerView = findViewById(R.id.rvTrains)
        if (newTrains.isEmpty()) {
            val noJourneysFoundText: TextView = findViewById(R.id.noJourneysFoundText)
            noJourneysFoundText.visibility = View.VISIBLE
            rvTrains.visibility = View.GONE
            return
        }
        val trainAdapter = TrainAdapter(newTrains)
        rvTrains.adapter = trainAdapter
        rvTrains.layoutManager = LinearLayoutManager(this)
    }



}
