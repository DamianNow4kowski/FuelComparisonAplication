package com.example.fuelcomparison.controllers


import android.location.Geocoder
import com.example.fuelcomparison.R
import com.example.fuelcomparison.data.GasStation
import com.example.fuelcomparison.data.Response
import com.example.fuelcomparison.fragments.MapFragment
import com.example.fuelcomparison.interfaces.AsyncConnectionCallback
import com.example.fuelcomparison.model.MapFragmentModel
import com.example.fuelcomparison.source.AsyncConnectionTask
import com.example.fuelcomparison.source.AsyncConnectionTaskFactory
import com.example.fuelcomparison.source.RequestBuilder
import com.example.fuelcomparison.source.ResponseStatus
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type


class MapFragmentController(
    private val mapView: MapFragment,
    private var mapModel: MapFragmentModel? = null,
    private val geocoder: Geocoder, private val taskFactory: AsyncConnectionTaskFactory
) : AsyncConnectionCallback {

    override fun processRequestResponse(response: Response?) {
        handleRetrieveGasStationsResponse(response!!.responseContent)
    }

    override fun processConnectionTimeout() {}

    fun processRetrieveGasStations(visibleRegion: LatLngBounds) {
        val requestBuilder =
            RequestBuilder(mapView.getString(R.string.retrieveStationsUrl))
        requestBuilder.putParameter("latNorth", visibleRegion.northeast.latitude.toString())
        requestBuilder.putParameter("longEast", visibleRegion.northeast.longitude.toString())
        requestBuilder.putParameter("latSouth", visibleRegion.southwest.latitude.toString())
        requestBuilder.putParameter("longWest", visibleRegion.southwest.longitude.toString())
        taskFactory.create(this, AsyncConnectionTask.RequestType.RETRIEVE_GAS_STATIONS)
            .execute(requestBuilder.build())
    }

    private fun handleRetrieveGasStationsResponse(response: String) {
        try {
            val responseJson = JSONObject(response)
            val status = responseJson.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                handleSuccessfulRetrieveGasStations(responseJson.getString(ResponseStatus.Key.CONTENT))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun handleSuccessfulRetrieveGasStations(responseContent: String) {
        val listType: Type =
            object : TypeToken<List<GasStation?>?>() {}.getType()
        val stations: List<GasStation> =
            Gson().fromJson(responseContent, listType)
        mapView.clearCurrentStations()
        addGasStations(stations)
    }

    fun clearGasStations(googleMap: GoogleMap) {
        googleMap.clear()
        mapModel!!.markersStations.clear()
    }

    private fun addGasStations(stations: List<GasStation>) {
        for (station in stations) {
            val marker: Marker = mapView.createGasStationMarker(station)
            mapModel!!.markersStations[marker] = station
        }
    }
}