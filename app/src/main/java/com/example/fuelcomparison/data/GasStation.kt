package com.example.fuelcomparison.data

import java.io.Serializable

class GasStation : Serializable {
    var id: Long = 0
    var name = ""
    var address = ""
    var city = ""
    var latitude = 0.0
    var longitude = 0.0
    override fun toString(): String {
        return "GasStation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                '}'
    }

    companion object {
        private const val serialVersionUID = -7619773757862241235L
    }
}