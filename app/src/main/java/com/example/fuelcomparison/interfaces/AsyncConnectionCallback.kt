package com.example.fuelcomparison.interfaces

import com.example.fuelcomparison.data.Response

interface AsyncConnectionCallback {
    fun processRequestResponse(response: Response?)
    fun processConnectionTimeout()
}