package com.example.fuelcomparison.data

import java.io.Serializable

class GasStation : Serializable {
    var id: Long = 0
    var name = ""
    var address = ""
    var city = ""
    var latitude = 0.0
    var longitude = 0.0
    var accepted = false
    var openFrom: String = ""
    var openTo: String = ""
    var hasPetrol95: Boolean = false
    var hasPetrol98: Boolean = false
    var hasDieselFuel: Boolean = false
    var hasNaturalGas: Boolean = false
    var isForElectricCars: Boolean = false
    var isForDisabledPeople: Boolean = false
    var fuels: List<Fuel> = emptyList()

    companion object {
        private const val serialVersionUID = -7619773757862241235L
    }

    override fun toString(): String {
        return "GasStation(id=$id, name='$name', address='$address', city='$city', latitude=$latitude, longitude=$longitude, accepted=$accepted, openFrom='$openFrom', openTo='$openTo', hasPetrol95=$hasPetrol95, hasPetrol98=$hasPetrol98, hasDieselFuel=$hasDieselFuel, hasNaturalGas=$hasNaturalGas, isForElectricCars=$isForElectricCars, isForDisabledPeople=$isForDisabledPeople)"
    }
}