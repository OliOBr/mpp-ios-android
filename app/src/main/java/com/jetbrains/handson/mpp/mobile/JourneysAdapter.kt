package com.jetbrains.handson.mpp.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JourneysAdapter(private val journeys: List<Journey>): RecyclerView.Adapter<JourneysAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val departureStation: TextView = itemView.findViewById<TextView>(R.id.departureStation)
        val arrivalStation: TextView = itemView.findViewById<TextView>(R.id.arrivalStation)
        val departureTime: TextView = itemView.findViewById<TextView>(R.id.departureTime)
        val arrivalTime: TextView = itemView.findViewById<TextView>(R.id.arrivalTime)
        val status: TextView = itemView.findViewById<TextView>(R.id.status)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_journey, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return journeys.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journey: Journey = journeys[position]
        holder.arrivalStation.text = journey.destinationStation
        holder.departureStation.text = journey.originStation
        holder.departureTime.text = journey.departureTime
        holder.arrivalTime.text = journey.arrivalTime
        holder.status.text = journey.status
    }

}