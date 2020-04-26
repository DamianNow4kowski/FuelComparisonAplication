package com.example.fuelcomparison.model;

import com.example.fuelcomparison.data.GasStation;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

public class MapFragmentModel {
    public Map<Marker, GasStation> markersStations = new HashMap<>();
}
