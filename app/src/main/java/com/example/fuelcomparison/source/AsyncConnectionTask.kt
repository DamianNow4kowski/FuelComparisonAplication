package com.example.fuelcomparison.source

import android.os.AsyncTask
import android.os.Handler
import com.example.fuelcomparison.data.Request
import com.example.fuelcomparison.data.Response
import com.example.fuelcomparison.interfaces.AsyncConnectionCallback
import java.io.*
import java.net.URL
import java.net.URLConnection
import java.net.URLDecoder

class AsyncConnectionTask internal constructor(
    private val delegate: AsyncConnectionCallback,
    private val requestType: RequestType
) : AsyncTask<Request?, Void?, Response>() {
    override fun onPreExecute() {
        super.onPreExecute()
        startConnectionTimeoutHandler()
    }

    override fun doInBackground(vararg params: Request?): Response {
        return try {
            val connection = initConnection(params[0]?.address)
            sendData(connection, params[0]!!.params)
            receiveData(connection)
        } catch (e: IOException) {
            e.printStackTrace()
            Response()
        }
    }

    override fun onPostExecute(result: Response) {
        super.onPostExecute(result)
        result.requestType = requestType
        delegate.processRequestResponse(result)
    }

    private fun startConnectionTimeoutHandler() {
        val handler = Handler()
        handler.postDelayed({
            if (status == Status.RUNNING) {
                cancel(true)
                delegate.processConnectionTimeout()
            }
        }, TIME_LIMIT_MS.toLong())
    }

    @Throws(IOException::class)
    private fun initConnection(urlAddress: String?): URLConnection {
        val url = URL(urlAddress)
        val connection = url.openConnection()
        connection.doOutput = true
        return connection
    }

    @Throws(IOException::class)
    private fun sendData(
        connection: URLConnection,
        postRequest: String

    ) {
        val writer =
            BufferedWriter(OutputStreamWriter(connection.getOutputStream()))
        writer.write(postRequest)
        writer.close()
    }

    @Throws(IOException::class)
    private fun receiveData(connection: URLConnection): Response {
        val reader =
            BufferedReader(InputStreamReader(connection.getInputStream()))
        val response =
            Response()
        val stringBuilder = StringBuilder()
        var responseLine: String?
        while (reader.readLine().also { responseLine = it } != null) stringBuilder.append(
            responseLine
        )
        val receivedData = stringBuilder.toString()
        response.responseContent = URLDecoder.decode(receivedData, "UTF8")
        return response
    }

    enum class RequestType {
        NONE, LOGIN, REGISTER, RETRIEVE_GAS_STATIONS
    }

    companion object {
        private const val TIME_LIMIT_MS = 10000
    }

}