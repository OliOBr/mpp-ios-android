package com.jetbrains.handson.mpp.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainAdapter(private val trains: List<Train>): RecyclerView.Adapter<TrainAdapter.ViewHolder>(){
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
        val contactView = inflater.inflate(R.layout.item_train, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return trains.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val train: Train = trains[position]
        holder.arrivalStation.text = train.destinationStation
        holder.departureStation.text = train.originStation
        holder.departureTime.text = train.departureTime
        holder.arrivalTime.text = train.arrivalTime
        holder.status.text = train.status
    }

}