package com.jetbrains.handson.mpp.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainAdapter(private val trains: List<Train>): RecyclerView.Adapter<TrainAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val departureStation: TextView = itemView.findViewById<TextView>(R.id.departure_station)
        val arrivalStation: TextView = itemView.findViewById<TextView>(R.id.arrival_station)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_train, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return trains.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val train: Train = trains[position]

        holder.arrivalStation.text = train.arrivalStation
        holder.departureStation.text = train.departureStation
    }

}