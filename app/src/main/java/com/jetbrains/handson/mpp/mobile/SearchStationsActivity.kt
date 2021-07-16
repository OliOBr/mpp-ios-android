package com.jetbrains.handson.mpp.mobile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar


class SearchStationsActivity : AppCompatActivity(), ApplicationContract.SearchStationsView {

    lateinit var presenter: ApplicationPresenter

    lateinit var topAppBar: MaterialToolbar

    private val urlForStationsData: String = "https://mobile-api-softwire2.lner.co.uk/v1/stations"
    private var stations: MutableList<Station> = mutableListOf()
    private var filteredStations: MutableList<Station> = mutableListOf()

    lateinit var adapter: StationsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stations)
        topAppBar = findViewById(R.id.topAppBar)

        topAppBar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        presenter = ApplicationPresenter()
        presenter.getAndListStationsData(this, urlForStationsData)
        filteredStations = stations
        val listView = findViewById<RecyclerView>(R.id.listView)
        val searchText = findViewById<EditText>(R.id.searchText)

        adapter = StationsAdapter(filteredStations,this)
        listView.adapter = adapter

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {}
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                filteredStations.clear()
                filteredStations = stations.toList().filter{it.stationName.contains(s)}.toMutableList()
                adapter.setItems(filteredStations)
                adapter.notifyDataSetChanged()
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun listStationsInListView(stationsData: List<Station>) {
        filteredStations.clear()
        filteredStations.addAll(stationsData)
        adapter.notifyDataSetChanged()
    }

}