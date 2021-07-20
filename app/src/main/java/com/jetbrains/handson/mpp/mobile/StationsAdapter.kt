package com.jetbrains.handson.mpp.mobile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.w3c.dom.Text
import java.lang.Math.round

class StationsAdapter(private var stations: List<Station>, var context: Context): RecyclerView.Adapter<StationsAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationName: TextView = itemView.findViewById<TextView>(R.id.stationName)
        val distanceFromOrigin: TextView = itemView.findViewById<TextView>(R.id.distanceFromOrigin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_station, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station: Station = stations[position]
        holder.itemView.setOnClickListener {
            val stationName = stations[position].stationName
            val crs = stations[position].crs
            val searchAndCRSResult = Intent().putExtra("ResultCRS", crs)
                .putExtra("Result", stationName)

            (context as Activity).setResult(Activity.RESULT_OK, searchAndCRSResult)
            (context as Activity).finish()}
        holder.stationName.text = station.stationName
        if (station.distanceFromLocation != null) {
            val distanceFromLocation = station.distanceFromLocation
            holder.distanceFromOrigin.text = "$distanceFromLocation miles"
        }
    }

    fun setItems(stationsData: List<Station>) {
        this.stations  = stationsData
    }

}