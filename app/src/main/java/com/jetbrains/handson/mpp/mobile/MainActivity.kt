package com.jetbrains.handson.mpp.mobile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class MainActivity : AppCompatActivity(), ApplicationContract.View {

    lateinit var departureStationText: EditText
    lateinit var arrivalStationText: EditText

    val departureStationStart = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            departureStationText.setText(result.data!!.getStringExtra("Result"))
        }
    }

    val arrivalStationStart = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            arrivalStationText.setText(result.data!!.getStringExtra("Result"))
        }
    }



override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        departureStationText = findViewById(R.id.departureStationText)
        arrivalStationText = findViewById(R.id.arrivalStationText)
        departureStationText.setOnClickListener{
            departureStationStart.launch(Intent(this,SearchStations::class.java))
        }
        arrivalStationText.setOnClickListener{
            arrivalStationStart.launch(Intent(this,SearchStations::class.java))
        }

        val button: Button = findViewById(R.id.button)

        button.setOnClickListener{getData(this)}
    }

    override fun setLabel(text: String) {
        //findViewById<TextView>(R.id.main_text).text = text
    }


    // TODO: to catch exception for when originDest same as targetDest, or app crashes.
    fun getData(view: ApplicationContract.View) {
        val departureStation=departureStationText.text.toString()
        val arrivalStation = arrivalStationText.text.toString()
        val url = getAPIURLWithSelectedStations(arrivalStation,departureStation)
        val presenter = ApplicationPresenter()
        print(presenter.getData(view,url))
    }

    override fun updateTrainsRecycleView(newTrains: List<Train>) {
        val rvTrains: RecyclerView = findViewById(R.id.rvTrains)
        val trainAdapter = TrainAdapter(newTrains)
        rvTrains.adapter = trainAdapter
        rvTrains.layoutManager = LinearLayoutManager(this)
    }



}
