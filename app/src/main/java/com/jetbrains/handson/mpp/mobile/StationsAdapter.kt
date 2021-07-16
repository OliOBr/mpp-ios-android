package com.jetbrains.handson.mpp.mobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StationsAdapter(private var stations: List<Station>, var context: Context): RecyclerView.Adapter<StationsAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationName: TextView = itemView.findViewById<TextView>(R.id.stationName)

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
    }

    fun setItems(stationsData: List<Station>) {
        this.stations  = stationsData
    }

}