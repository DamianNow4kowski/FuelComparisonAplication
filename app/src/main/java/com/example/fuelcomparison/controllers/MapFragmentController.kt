package com.example.fuelcomparison.controllers


import android.annotation.SuppressLint
import android.location.Geocoder
import com.example.fuelcomparison.R
import com.example.fuelcomparison.activities.MainActivity
import com.example.fuelcomparison.data.GasStation
import com.example.fuelcomparison.data.Response
import com.example.fuelcomparison.fragments.MapFragment
import com.example.fuelcomparison.interfaces.AsyncConnectionCallback
import com.example.fuelcomparison.model.MapFragmentModel
import com.example.fuelcomparison.source.*
import com.example.fuelcomparison.source.Util.showToast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.Type
import java.util.*


class MapFragmentController(
    private val mapView: MapFragment,
    private var mapModel: MapFragmentModel? = null,
    private val geocoder: Geocoder, private val taskFactory: AsyncConnectionTaskFactory
) : AsyncConnectionCallback {

    override fun processRequestResponse(response: Response?) {
        if (response!!.requestType == AsyncConnectionTask.RequestType.GAS_STATION_DATA) handleAddGasStationResponse(
            response.responseContent
        )
        else if (response.requestType == AsyncConnectionTask.RequestType.RETRIEVE_GAS_STATIONS) handleRetrieveGasStationsResponse(
            response.responseContent
        )
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

    fun handleMapMarkerClick(marker: Marker?) {
        val gasStation = mapModel!!.markersStations[marker]
        gasStation?.let { launchGasStationInfoActivity(it) }
    }

    private fun launchGasStationInfoActivity(gasStation: GasStation) {
        val activity = mapView.activity as MainActivity?
        activity!!.startGasStationInfoActivity(gasStation)
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
            object : TypeToken<List<GasStation?>?>() {}.type
        val stations: List<GasStation> =
            Gson().fromJson(responseContent, listType)

        val filteredStations = filterStations(stations)

        mapView.clearCurrentStations()
        addGasStations(filteredStations)
    }

    @SuppressLint("DefaultLocale")
    private fun filterStations(stations: List<GasStation>): List<GasStation> {
        var stationFilter = FilterState.stationFilter
        var filteredStations = stations

        filteredStations = filterByDistance(filteredStations)
        filteredStations = filterByPetrols(filteredStations)

        if (stationFilter?.forDisabledPeople!!) {
            filteredStations =
                filteredStations.filter {
                    it.isForDisabledPeople == stationFilter.forDisabledPeople
                }
        }
        return filteredStations
    }

    fun calcDistance(
        latA: Double?,
        longA: Double?,
        latB: Double,
        longB: Double
    ): Double {
        val theDistance =
            Math.sin(Math.toRadians(latA?.toDouble()!!)) *
                    Math.sin(Math.toRadians(latB.toDouble())) +
                    Math.cos(Math.toRadians(latA.toDouble())) *
                    Math.cos(Math.toRadians(latB.toDouble())) *
                    Math.cos(Math.toRadians(longA?.minus(longB.toDouble())!!))
        return (Math.toDegrees(Math.acos(theDistance)) * 69.09 * 1.6093)
    }

    fun clearGasStations(googleMap: GoogleMap) {
        googleMap.clear()
        mapModel!!.markersStations.clear()
    }

    private fun filterByDistance(stations: List<GasStation>): List<GasStation> {
        val stationFilter = FilterState.stationFilter
        var filteredStations = stations
        val currentLat = mapView.googleMap?.cameraPosition?.target?.latitude
        val currentLon = mapView.googleMap?.cameraPosition?.target?.longitude
        filteredStations = filteredStations.filter {
            calcDistance(
                currentLat,
                currentLon,
                it.latitude,
                it.longitude
            ) < stationFilter?.distance!!
        }
        return filteredStations
    }

    private fun filterByPrices(stations: List<GasStation>): List<GasStation> {
        val stationFilter = FilterState.stationFilter
        var filteredStations = stations
        filteredStations = filteredStations.filter {
            val fuels = it.fuels
            var areAllCheap = true
            for (fuel in fuels) {
                if (fuel.price >= stationFilter?.maxPrice!!) {
                    areAllCheap = false
                    break
                }
            }
            areAllCheap
        }

        return filteredStations
    }

    private fun filterByPetrols(stations: List<GasStation>): List<GasStation> {
        val stationFilter = FilterState.stationFilter
        var filteredStations = stations

        if (!stationFilter?.hasPetrol95!! && !stationFilter.hasPetrol98!! && !stationFilter?.hasDieselFuel!! && !stationFilter?.hasNaturalGas!!) {
            filteredStations = filterByPrices(filteredStations)
        } else {
            if (stationFilter?.hasPetrol95!!) {
                filteredStations = filterByPetrol(filteredStations, "petrol 95")
            }
            if (stationFilter.hasPetrol98) {
                filteredStations = filterByPetrol(filteredStations, "petrol 98")
            }
            if (stationFilter.hasNaturalGas) {
                filteredStations = filterByPetrol(filteredStations, "natural gas")
            }
            if (stationFilter.hasDieselFuel) {
                filteredStations = filterByPetrol(filteredStations, "diesel")
            }
        }
        return filteredStations
    }

    private fun filterByPetrol(
        filteredStations: List<GasStation>,
        fuelName: String
    ): List<GasStation> {
        val stationFilter = FilterState.stationFilter
        return filteredStations.filter {
            val fuels = it.fuels
            var found = false
            for (fuel in fuels) {
                if (fuel.name?.toLowerCase()
                        .equals(fuelName) && fuel.price <= stationFilter?.maxPrice!!
                ) {
                    found = true
                }
            }
            found
        }
    }

    private fun addGasStations(stations: List<GasStation>) {
        for (station in stations) {
            val marker: Marker = mapView.createGasStationMarker(station)
            mapModel!!.markersStations[marker] = station
        }
    }

    fun createGasStationFromLatLng(position: LatLng): Optional<GasStation>? {
        return try {
            val gasStation = GasStation()
            val address =
                geocoder.getFromLocation(position.latitude, position.longitude, 1)[0]
            gasStation.address = address.getAddressLine(0)
            gasStation.city = address.locality
            gasStation.latitude = position.latitude
            gasStation.longitude = position.longitude
            Optional.of(gasStation)
        } catch (e: IOException) {
            Optional.empty()
        } catch (e: IndexOutOfBoundsException) {
            Optional.empty()
        }
    }

    fun processAddGasStation(gasStation: GasStation?) {
        val stationJson = Gson().toJson(gasStation)
        val requestBuilder =
            RequestBuilder(mapView.getString(R.string.addStationUrl))
        requestBuilder.putParameter("stationData", stationJson)
        taskFactory.create(this, AsyncConnectionTask.RequestType.GAS_STATION_DATA)
            .execute(requestBuilder.build())
    }

    private fun handleAddGasStationResponse(response: String) {
        try {
            val responseJson = JSONObject(response)
            val status = responseJson.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                handleSuccessfulAddStation(responseJson.getString(ResponseStatus.Key.CONTENT))
            } else {
                handleUnsuccessfulAddStation(responseJson.getString(ResponseStatus.Key.REASON))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun handleSuccessfulAddStation(responseContent: String) {
        showToast(mapView.activity, mapView.getString(R.string.addedGasStationSuccessful))
        val gasStation = Gson().fromJson(responseContent, GasStation::class.java)
        val marker = mapView.createGasStationMarker(gasStation)
        mapModel!!.markersStations[marker] = gasStation
    }

    private fun handleUnsuccessfulAddStation(errorCode: String) {
        when (errorCode) {
            ResponseStatus.AddGasStation.DB_INSERT_EXCEPTION -> showToast(
                mapView.activity,
                mapView.getString(R.string.addGasStationError)
            )
        }
    }
}