package com.example.fuelcomparison.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelcomparison.R
import com.example.fuelcomparison.data.Fuel

class GasStationFuelsAdapter(
    @param:NonNull private val context: Context,
    fuels: MutableList<Fuel>
) : RecyclerView.Adapter<GasStationFuelsAdapter.ViewHolder?>() {
    private val fuels: MutableList<Fuel>

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.fuel_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        @NonNull holder: ViewHolder,
        position: Int
    ) {
        val fuel: Fuel = fuels[position]
        holder.fuelName.setText(fuel.name)
        holder.fuelPrice.setText(fuel.price.toString())
    }

    fun addFuels(fuels: List<Fuel>?) {
        this.fuels.clear()
        this.fuels.addAll(fuels!!)
        notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val fuelName: TextView
        val fuelPrice: TextView
        override fun onClick(v: View) {
//            showFuelPriceDialog()
        }

        init {
            fuelName = itemView.findViewById(R.id.fuelName)
            fuelPrice = itemView.findViewById(R.id.fuelPrice)
            itemView.setOnClickListener(this)
        }
    }

    init {
        this.fuels = fuels
    }

    override fun getItemCount(): Int {
        return fuels.size
    }
}
