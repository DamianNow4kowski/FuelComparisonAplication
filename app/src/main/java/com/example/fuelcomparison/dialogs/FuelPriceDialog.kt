package com.example.fuelcomparison.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.fuelcomparison.R
import com.example.fuelcomparison.activities.GasStationInfoActivity
import com.example.fuelcomparison.data.Fuel
import com.example.fuelcomparison.source.Util.showToast

class FuelPriceDialog(context: Context?, fuel: Fuel) :
    Dialog(context!!, R.style.DialogTheme) {
    private var newFuelPrice: EditText? = null
    private val fuel: Fuel
    private val activity: GasStationInfoActivity?
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fuel_price)
        newFuelPrice = findViewById(R.id.newFuelPrice)
        setAddNewButtonListener()
        setConfirmButtonListener()
    }

    private fun setAddNewButtonListener() {
        val addNewFuelPriceButton =
            findViewById<Button>(R.id.addNewFuelPriceButton)
        addNewFuelPriceButton.setOnClickListener { view: View? ->
            if (newFuelPrice!!.text.isNotEmpty()) {
                sendFuelPrice(newFuelPrice!!.text.toString().toFloat())
            } else {
                showToast(activity, activity!!.getString(R.string.writeFuelPrice))
            }
            dismiss()
        }
    }

    private fun setConfirmButtonListener() {
        val confirmFuelPriceButton =
            findViewById<Button>(R.id.confirmFuelPriceButton)
        confirmFuelPriceButton.setOnClickListener { view: View? ->
            sendFuelPrice(fuel.price)
            dismiss()
        }
    }

    private fun sendFuelPrice(price: Float) {
        activity!!.saveFuelPrice(fuel.fuelId, price.toString())
    }

    init {
        this.fuel = fuel
        activity = context as GasStationInfoActivity?
    }
}