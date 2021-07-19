package com.jetbrains.handson.mpp.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar


class SearchStationsActivity : AppCompatActivity(), ApplicationContract.SearchStationsView {

    lateinit var presenter: ApplicationPresenter

    lateinit var topAppBar: MaterialToolbar

    private var stations: MutableList<Station> = mutableListOf()

    lateinit var adapter: ArrayAdapter<Station>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stations)
        topAppBar = findViewById(R.id.topAppBar)

        topAppBar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        presenter = ApplicationPresenter()
        presenter.getAndListStationsData(this)

        val listView = findViewById<ListView>(R.id.listView)
        val searchText = findViewById<EditText>(R.id.searchText)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stations)
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
                adapter.filter.filter(s)
            }
            override fun afterTextChanged(s: Editable) {}
        })
        listView.setOnItemClickListener { _, _, position, _ ->
            val stationName = adapter.getItem(position)?.stationName
            val crs = adapter.getItem(position)?.crs
            val searchAndCRSResult = Intent().putExtra("ResultCRS", crs)
                    .putExtra("Result", stationName)
            setResult(Activity.RESULT_OK, searchAndCRSResult)
            finish()
        }
    }

    override fun listStationsInListView(stationsData: List<Station>) {
        stations.clear()
        stations.addAll(stationsData)
        adapter.notifyDataSetChanged()
    }

}