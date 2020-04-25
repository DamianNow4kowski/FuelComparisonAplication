package com.example.fuelcomparison.fragments

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fuelcomparison.R
import com.example.fuelcomparison.controllers.MapFragmentController
import com.example.fuelcomparison.source.AsyncConnectionTaskFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback,
    OnMapLongClickListener, OnMarkerClickListener, OnCameraIdleListener {
    private var controller: MapFragmentController? = null
    private var googleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = MapFragmentController(
            this,
            Geocoder(activity, Locale.getDefault()), AsyncConnectionTaskFactory()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap!!.setOnMapLongClickListener(this)
        this.googleMap!!.setOnMarkerClickListener(this)
        this.googleMap!!.setOnCameraIdleListener(this)
    }

    override fun onMapLongClick(latLng: LatLng) {}
    override fun onMarkerClick(marker: Marker): Boolean {
        return true
    }

    override fun onCameraIdle() {
        val cameraBounds = googleMap!!.projection.visibleRegion.latLngBounds
    }

    @Deprecated("")
    fun setController(controller: MapFragmentController?) {
        this.controller = controller
    }
}