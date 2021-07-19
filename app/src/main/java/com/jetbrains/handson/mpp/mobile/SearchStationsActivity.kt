package com.jetbrains.handson.mpp.mobile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher

import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar


class SearchStationsActivity : AppCompatActivity(), SearchStationsContract.View {

    lateinit var presenter: SearchStationsPresenter

    lateinit var topAppBar: MaterialToolbar

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


        presenter = SearchStationsPresenter()
        presenter.getAndListStationsData(this)

        filteredStations = stations
        val listView = findViewById<RecyclerView>(R.id.listView)

        val searchText = findViewById<EditText>(R.id.searchText)

        adapter = StationsAdapter(filteredStations,this)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(this)
        listView.addItemDecoration(
            DividerItemDecoration(
                listView.context,
                DividerItemDecoration.VERTICAL
            )
        )
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
                filteredStations.addAll(stations.toList().filter{ station -> station.stationName.toLowerCase().contains(s,ignoreCase = true)})
                adapter.notifyDataSetChanged()
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun listStationsInListView(stationsData: List<Station>) {
        filteredStations.clear()
        filteredStations.addAll(stationsData)
        stations = stationsData.toMutableList()
        adapter.notifyDataSetChanged()
    }

}