package com.example.fuelcomparison.data

import java.io.Serializable

class Fuel : Serializable {
    var fuelId: Long = 0
    var name: String? = null
    var price = 0f
    override fun toString(): String {
        return "Fuel{" +
                "fuel_id=" + fuelId +
                ", name=" + name +
                ", price=" + price +
                '}'
    }

    companion object {
        private const val serialVersionUID = 7747284152827699862L
    }
}