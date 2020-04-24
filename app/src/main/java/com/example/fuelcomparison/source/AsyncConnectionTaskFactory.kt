package com.example.fuelcomparison.source

import com.example.fuelcomparison.interfaces.AsyncConnectionCallback

class AsyncConnectionTaskFactory {
    fun create(
        callback: AsyncConnectionCallback?,
        requestType: AsyncConnectionTask.RequestType?
    ): AsyncConnectionTask {
        return AsyncConnectionTask(callback!!, requestType!!)
    }
}