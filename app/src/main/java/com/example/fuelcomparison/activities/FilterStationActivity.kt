package com.example.fuelcomparison.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.annotation.Nullable
import com.example.fuelcomparison.R
import com.example.fuelcomparison.data.StationFilter
import com.example.fuelcomparison.source.FilterState

class FilterStationActivity : Activity() {
    private var distance: EditText? = null
    private var maxPrice: EditText? = null
    private var hasPetrol95: CheckBox? = null
    private var hasPetrol98: CheckBox? = null
    private var hasDieselFuel: CheckBox? = null
    private var hasNaturalGas: CheckBox? = null
    private var forDisabledPeople: CheckBox? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_station_fitler)
        initializeGui()
    }

    fun handleFilterButtonClick(view: View?) {
        val distance = distance!!.text.toString().trim { it <= ' ' }.toDouble()
        val maxPrice = maxPrice!!.text.toString().trim { it <= ' ' }.toDouble()
        val hasPetrol95 = hasPetrol95!!.isChecked
        val hasPetrol98 = hasPetrol98!!.isChecked
        val hasDieselFuel = hasDieselFuel!!.isChecked
        val hasNaturalGas = hasNaturalGas!!.isChecked
        val forDisabledPeople = forDisabledPeople!!.isChecked

        val stationFilter = StationFilter(
            distance,
            maxPrice,
            hasPetrol95,
            hasPetrol98,
            hasDieselFuel,
            hasNaturalGas,
            forDisabledPeople
        )
        FilterState.setState(stationFilter)
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun initializeGui() {
        distance = findViewById(R.id.gasStationDistance)
        maxPrice = findViewById(R.id.gasStationMaxPrice)
        hasPetrol95 = findViewById(R.id.hasPetrol95)
        hasPetrol98 = findViewById(R.id.hasPetrol98)
        hasDieselFuel = findViewById(R.id.hasDieselFuel)
        hasNaturalGas = findViewById(R.id.hasNaturalGas)
        forDisabledPeople = findViewById(R.id.forDisabledPeople)
    }
}
