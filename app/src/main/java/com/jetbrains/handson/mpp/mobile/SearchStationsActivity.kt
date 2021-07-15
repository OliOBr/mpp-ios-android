package com.jetbrains.handson.mpp.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar


class SearchStationsActivity : AppCompatActivity() {

    lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stations)
        topAppBar = findViewById(R.id.topAppBar)

        topAppBar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        val listView = findViewById<ListView>(R.id.listView)
        val searchText = findViewById<EditText>(R.id.searchText)

        //TODO: call get stations from presenter and then change createFromResource to suitable ArrayAdapter constructor
        var arrayAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.stations_array,
            android.R.layout.simple_list_item_1
        )
        listView.adapter = arrayAdapter

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
                arrayAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        listView.setOnItemClickListener { parent, view, position, id ->
            val element = arrayAdapter.getItem(position)
            val searchResult = Intent().putExtra("Result",element)
            setResult(Activity.RESULT_OK,searchResult)
            finish()
        }
    }

}