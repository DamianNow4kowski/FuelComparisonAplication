package com.example.fuelcomparison.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.annotation.NonNull
import com.example.fuelcomparison.R
import com.example.fuelcomparison.data.GasStation
import com.example.fuelcomparison.fragments.MapFragment

class AddGasStationDialog(
    context: Context?,
    @NonNull mapFragment: MapFragment,
    gasStation: GasStation
) :
    Dialog(context!!, R.style.DialogTheme) {
    private var stationNameArea: EditText? = null
    private var stationStreetArea: EditText? = null
    private var stationCityArea: EditText? = null
    private var longitudeArea: EditText? = null
    private var latitudeArea: EditText? = null
    private var openFrom: EditText? = null
    private var openTo: EditText? = null
    private var hasPetrol95: CheckBox? = null
    private var hasPetrol98: CheckBox? = null
    private var hasDieselFuel: CheckBox? = null
    private var hasNaturalGas: CheckBox? = null
    private var isForDisabledPeople: CheckBox? = null
    private var isForElectricCars: CheckBox? = null
    private val mapFragment: MapFragment
    private val gasStation: GasStation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_station)
        stationNameArea = findViewById(R.id.gasStationName)
        stationStreetArea = findViewById(R.id.gasStationStreet)
        stationCityArea = findViewById(R.id.gasStationCity)
        longitudeArea = findViewById(R.id.gasStationLongitude)
        latitudeArea = findViewById(R.id.gasStationLatitude)
        openFrom = findViewById(R.id.gasStationOpenFrom)
        openTo = findViewById(R.id.gasStationOpenTo)
        hasPetrol95 = findViewById(R.id.hasPetrol95)
        hasPetrol98 = findViewById(R.id.hasPetrol98)
        hasDieselFuel = findViewById(R.id.hasDieselFuel)
        hasNaturalGas = findViewById(R.id.hasNaturalGas)
        isForDisabledPeople = findViewById(R.id.isGasStationForDisabledPeople)
        isForElectricCars = findViewById(R.id.isGasStationForElectricCars)
        setAddStationButtonListener()
        initializeViewData()
    }

    private fun setAddStationButtonListener() {
        val addStationButton =
            findViewById<Button>(R.id.addStationButton)
        addStationButton.setOnClickListener { view: View? ->
            updateGasStationData()
            mapFragment.processAddGasStation(gasStation)
            dismiss()
        }
    }

    private fun initializeViewData() {
        stationStreetArea?.setText(gasStation.address)
        stationCityArea?.setText(gasStation.city)
        longitudeArea?.setText(gasStation.longitude.toString())
        latitudeArea?.setText(gasStation.latitude.toString())
    }

    private fun updateGasStationData() {
        gasStation.name = stationNameArea!!.text.toString()
        gasStation.address = stationStreetArea!!.text.toString()
        gasStation.city = stationCityArea!!.text.toString()
        gasStation.longitude = java.lang.Double.valueOf(longitudeArea!!.text.toString())
        gasStation.latitude = java.lang.Double.valueOf(latitudeArea!!.text.toString())
        gasStation.openFrom = openFrom!!.text.toString().trim { it <= ' ' }
        gasStation.openTo = openTo!!.text.toString().trim { it <= ' ' }
        gasStation.hasPetrol95 = hasPetrol95!!.isChecked
        gasStation.hasPetrol98 = hasPetrol98!!.isChecked
        gasStation.hasDieselFuel = hasDieselFuel!!.isChecked
        gasStation.hasNaturalGas = hasNaturalGas!!.isChecked
        gasStation.isForDisabledPeople = isForDisabledPeople!!.isChecked
        gasStation.isForElectricCars = isForElectricCars!!.isChecked
    }

    init {
        this.gasStation = gasStation
        this.mapFragment = mapFragment
    }
}