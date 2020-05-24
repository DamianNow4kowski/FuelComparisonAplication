package com.example.fuelcomparison.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelcomparison.R
import com.example.fuelcomparison.activities.GasStationInfoActivity
import com.example.fuelcomparison.activities.MainActivity
import com.example.fuelcomparison.data.GasStation
import com.example.fuelcomparison.enums.IntentKey

class FavouriteStationsAdapter(
    private val context: Context?,
    private val gasStations: MutableList<GasStation>
) : RecyclerView.Adapter<FavouriteStationsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.station_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val gasStation = gasStations[position]
        holder.gasStationName.text = gasStation.name
        holder.gasStationAddress.text = gasStation.address
    }

    override fun getItemCount(): Int {
        return gasStations.size
    }

    fun clear() {
        gasStations.clear()
        notifyDataSetChanged()
    }

    fun addStations(stations: List<GasStation>) {
        gasStations.addAll(stations)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val gasStationName: TextView
        val gasStationAddress: TextView
        override fun onClick(v: View) {
            val activity = context as MainActivity
            val intent = Intent(activity, GasStationInfoActivity::class.java)
            intent.putExtra(IntentKey.GAS_STATION.name, gasStations[adapterPosition])
            activity.startActivity(intent)
        }

        init {
            gasStationName = itemView.findViewById(R.id.gasStationName)
            gasStationAddress = itemView.findViewById(R.id.gasStationAddress)
            itemView.setOnClickListener(this)
        }
    }

}