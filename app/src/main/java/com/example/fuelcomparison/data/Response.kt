package com.example.fuelcomparison.data

import com.example.fuelcomparison.source.AsyncConnectionTask

class Response {
    @JvmField
    var requestType = AsyncConnectionTask.RequestType.NONE

    @JvmField
    var responseContent = ""
}