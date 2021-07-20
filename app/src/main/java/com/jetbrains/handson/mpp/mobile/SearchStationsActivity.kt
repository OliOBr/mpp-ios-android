package com.jetbrains.handson.mpp.mobile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar


class SearchStationsActivity : AppCompatActivity(), SearchStationsContract.View {

    lateinit var presenter: SearchStationsPresenter

    lateinit var topAppBar: MaterialToolbar

    private var stations: List<Station> = mutableListOf()
    private var filteredStations: MutableList<Station> = mutableListOf()

    lateinit var adapter: StationsAdapter
    lateinit var locationManager: LocationManager


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

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        filteredStations = stations.toMutableList()
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
        addDistancesToStations(stationsData)
    }

    fun continueListStationsInListView(stationsData: List<Station>) {
        filteredStations.addAll(stationsData.sortedWith(compareBy<Station,Double?>(nullsLast()) { it.distanceFromLocation }))
        stations = filteredStations.toList()
        adapter.notifyDataSetChanged()
    }
    fun addDistancesToStations(stationsData: List<Station>) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, object :
                LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if (location != null) {
                            for(station in stationsData) {
                                val distanceInMiles = station.getDistanceFromLocation(
                                    location.longitude,
                                    location.latitude
                                )
                                if (distanceInMiles != null) {
                                    station.distanceFromLocation = distanceInMiles
                                }
                            }
                        }
                        continueListStationsInListView(stationsData)
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {
                    }

                    override fun onProviderDisabled(provider: String?) {
                    }
                }, null)
            }


}