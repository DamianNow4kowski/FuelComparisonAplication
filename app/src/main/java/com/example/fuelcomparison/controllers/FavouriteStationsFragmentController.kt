package com.example.fuelcomparison.controllers

import com.example.fuelcomparison.R
import com.example.fuelcomparison.adapters.FavouriteStationsAdapter
import com.example.fuelcomparison.data.GasStation
import com.example.fuelcomparison.data.Response
import com.example.fuelcomparison.fragments.FavouriteStationsFragment
import com.example.fuelcomparison.interfaces.AsyncConnectionCallback
import com.example.fuelcomparison.source.*
import com.example.fuelcomparison.source.Util.showToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class FavouriteStationsFragmentController(
    private val fragment: FavouriteStationsFragment,
    factory: AsyncConnectionTaskFactory
) : AsyncConnectionCallback {
    private val stationsAdapter: FavouriteStationsAdapter
    private val taskFactory: AsyncConnectionTaskFactory
    override fun processRequestResponse(response: Response?) {
        when (response!!.requestType) {
            AsyncConnectionTask.RequestType.RETRIEVE_FAV_STATIONS -> handleRetrieveFavStations(
                response.responseContent
            )
        }
    }

    override fun processConnectionTimeout() {
        showToast(fragment.activity, fragment.getString(R.string.connectionTimeout))
    }

    fun retrieveFavouriteGasStations() {
        val requestBuilder =
            RequestBuilder(fragment.getString(R.string.retrieveFavouriteStations))
        requestBuilder.putParameter("userId", "" + UserDataHolder.userData.id)
        taskFactory.create(this, AsyncConnectionTask.RequestType.RETRIEVE_FAV_STATIONS)
            .execute(requestBuilder.build())
    }

    private fun handleRetrieveFavStations(response: String) {
        try {
            val responseJson = JSONObject(response)
            val status =
                responseJson.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                handleSuccessfulRetrieveGasStations(responseJson.getString(ResponseStatus.Key.CONTENT))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun handleSuccessfulRetrieveGasStations(responseContent: String) {
        val listType =
            object : TypeToken<List<GasStation?>?>() {}.type
        val stations =
            Gson().fromJson<List<GasStation>>(responseContent, listType)
        clearGasStations()
        addGasStations(stations)
    }

    private fun clearGasStations() {
        stationsAdapter.clear()
    }

    private fun addGasStations(stations: List<GasStation>) {
        stationsAdapter.addStations(stations)
    }

    init {
        val gasStations: List<GasStation> = ArrayList()
        stationsAdapter = FavouriteStationsAdapter(fragment.activity,
            gasStations as MutableList<GasStation>
        )
        taskFactory = factory
        fragment.setAdapter(stationsAdapter)
    }
}