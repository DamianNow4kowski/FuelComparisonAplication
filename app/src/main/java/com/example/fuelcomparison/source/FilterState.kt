package com.example.fuelcomparison.source

import com.example.fuelcomparison.data.StationFilter

object FilterState {
    public var stationFilter: StationFilter? = StationFilter()

    fun setState(stationFilter: StationFilter?) {
        this.stationFilter = stationFilter
    }
}