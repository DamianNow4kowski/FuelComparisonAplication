package com.example.fuelcomparison.data

class StationFilter(
    var distance: Double = 10.0,
    var maxPrice: Double = 5.0,
    var hasPetrol95: Boolean = false,
    var hasPetrol98: Boolean = false,
    var hasDieselFuel: Boolean = false,
    var hasNaturalGas: Boolean = false,
    var forDisabledPeople: Boolean = false,
    var favourite: Boolean = false
) {

}