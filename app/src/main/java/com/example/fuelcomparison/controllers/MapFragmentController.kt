package com.example.fuelcomparison.controllers

import android.location.Geocoder
import com.example.fuelcomparison.data.Response
import com.example.fuelcomparison.fragments.MapFragment
import com.example.fuelcomparison.interfaces.AsyncConnectionCallback
import com.example.fuelcomparison.source.AsyncConnectionTaskFactory

class MapFragmentController(
    private val mapView: MapFragment,
    private val geocoder: Geocoder, private val taskFactory: AsyncConnectionTaskFactory
) : AsyncConnectionCallback {
    override fun processRequestResponse(response: Response?) {}
    override fun processConnectionTimeout() {}

}