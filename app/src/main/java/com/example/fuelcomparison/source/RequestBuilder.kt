package com.example.fuelcomparison.source

import com.example.fuelcomparison.data.Request
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

class RequestBuilder(urlAddress: String?) {
    private val requestParameters: MutableMap<String, String>
    private val request: Request
    fun putParameter(key: String, value: String?) {
        try {
            val encodedValue = URLEncoder.encode(value, "UTF-8")
            requestParameters[key] = encodedValue
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    fun build(): Request {
        val iterator: Iterator<Map.Entry<String, String>> =
            requestParameters.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.key.isEmpty() || entry.value.isEmpty()) continue
            request.params += entry.key + "=" + entry.value
            if (iterator.hasNext()) request.params += "&"
        }
        return request
    }

    init {
        requestParameters = LinkedHashMap()
        request = Request()
        request.address = urlAddress!!
    }
}