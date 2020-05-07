package com.example.fuelcomparison.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelcomparison.R
import com.example.fuelcomparison.adapters.GasStationCommentsAdapter
import com.example.fuelcomparison.adapters.GasStationFuelsAdapter
import com.example.fuelcomparison.controllers.GasStationInfoController
import com.example.fuelcomparison.data.GasStation
import com.example.fuelcomparison.enums.IntentKey
import com.example.fuelcomparison.source.AsyncConnectionTaskFactory
import java.lang.String

class GasStationInfoActivity : Activity() {
    protected var controller: GasStationInfoController? = null
    private var gasStationName: TextView? = null
    private var gasStationAddress: TextView? = null
    private var gasStationOpeningHours: TextView? = null
    private var hasPetrol95: CheckBox? = null
    private var hasPetrol98: CheckBox? = null
    private var hasDieselFuel: CheckBox? = null
    private var hasNaturalGas: CheckBox? = null
    private var isForDisabledPeople: CheckBox? = null
    private var isForElectricCars: CheckBox? = null
    private var gasStationFuels: RecyclerView? = null
    private var gasStationComments: RecyclerView? = null
    private var gasStation: GasStation? = null
    private var commentBody: EditText? = null
    private var commentRate: RatingBar? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_gas_station)
        gasStation = intent.getSerializableExtra(IntentKey.GAS_STATION.name) as GasStation
        initializeGui()
        fillGasStationInfo()
        controller = GasStationInfoController(this, AsyncConnectionTaskFactory(), gasStation!!)
        controller!!.loadGasStationFuels(gasStation!!.id)
        controller!!.loadGasStationComments(gasStation!!.id)
    }

    fun confirmStationClick(view: View?) {
//        controller!!.processConfirmStation(gasStation!!.id)
        controller!!.toggleFavouriteStatus()
    }

    fun setGasStationFuelsAdapter(adapter: GasStationFuelsAdapter?) {
        gasStationFuels!!.adapter = adapter
    }

    fun setGasStationCommentsAdapter(adapter: GasStationCommentsAdapter?) {
        gasStationComments!!.adapter = adapter
    }

    private fun initializeGui() {
        gasStationName = findViewById(R.id.gasStationName)
        gasStationAddress = findViewById(R.id.gasStationAddress)
        gasStationOpeningHours = findViewById(R.id.gasStationOpeningHours)
        hasPetrol95 = findViewById(R.id.hasPetrol95)
        hasPetrol98 = findViewById(R.id.hasPetrol98)
        hasDieselFuel = findViewById(R.id.hasDieselFuel)
        hasNaturalGas = findViewById(R.id.hasNaturalGas)
        isForDisabledPeople = findViewById(R.id.isGasStationForDisabledPeople)
        isForElectricCars = findViewById(R.id.isGasStationForElectricCars)
        gasStationFuels = findViewById(R.id.gasStationFuels)
        gasStationFuels!!.setHasFixedSize(true)
        gasStationFuels!!.layoutManager = LinearLayoutManager(this)
        gasStationComments = findViewById(R.id.gasStationOpinions)
        gasStationComments!!.setHasFixedSize(true)
        gasStationComments!!.layoutManager = LinearLayoutManager(this)

        commentBody = findViewById(R.id.commentBody)
        commentRate = findViewById(R.id.commentRate)

        findViewById<TextView?>(R.id.gasStationName)?.setOnClickListener { controller!!.toggleFavouriteStatus() }
    }


    fun handleSubmitCommentClick(view: View?) {
        val comment_body = commentBody!!.text.toString().trim { it <= ' ' }
        val comment_rate = commentRate!!.rating.toInt()
        controller!!.processCommentSubmitRequest(
            comment_body, comment_rate.toString(),
            String.valueOf(gasStation!!.id)
        )
    }

    @SuppressLint("SetTextI18n")
    private fun fillGasStationInfo() {
        gasStationName?.text = gasStation?.name
        gasStationAddress?.text = gasStation?.address
        gasStationOpeningHours?.text = gasStation?.openFrom.toString() + " - " + gasStation!!.openTo
        isForDisabledPeople!!.isChecked = gasStation!!.isForDisabledPeople
        isForElectricCars!!.isChecked = gasStation!!.isForElectricCars
    }
}
