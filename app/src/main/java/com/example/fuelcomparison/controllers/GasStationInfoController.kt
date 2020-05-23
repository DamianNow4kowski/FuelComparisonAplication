package com.example.fuelcomparison.controllers

import android.content.Intent
import android.util.Log
import android.view.View
import com.example.fuelcomparison.R
import com.example.fuelcomparison.activities.GasStationInfoActivity
import com.example.fuelcomparison.adapters.GasStationCommentsAdapter
import com.example.fuelcomparison.adapters.GasStationFuelsAdapter
import com.example.fuelcomparison.data.Comment
import com.example.fuelcomparison.data.Fuel
import com.example.fuelcomparison.data.GasStation
import com.example.fuelcomparison.data.Response
import com.example.fuelcomparison.interfaces.AsyncConnectionCallback
import com.example.fuelcomparison.interfaces.CommentClickListener
import com.example.fuelcomparison.source.*
import com.example.fuelcomparison.source.Util.showToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class GasStationInfoController(
    activity: GasStationInfoActivity,
    taskFactory: AsyncConnectionTaskFactory,
    gasStation: GasStation
) : AsyncConnectionCallback, CommentClickListener {
    private val activity: GasStationInfoActivity
    private val connectionTaskFactory: AsyncConnectionTaskFactory
    private val fuels: List<Fuel>
    private val comments: List<Comment>
    private val fuelsAdapter: GasStationFuelsAdapter
    private val commentsAdapter: GasStationCommentsAdapter
    private val gasStation: GasStation
    override fun processRequestResponse(response: Response?) {
        if (response != null) {
            when (response.requestType) {
                AsyncConnectionTask.RequestType.RETRIEVE_FUELS -> handleReceivedGasStationFuels(
                    response.responseContent
                )
                AsyncConnectionTask.RequestType.RETRIEVE_COMMENTS -> handleReceivedGasStationComments(
                    response.responseContent
                )
                AsyncConnectionTask.RequestType.SUBMIT_COMMENT -> handleCommentSubmitResponse(
                    response.responseContent
                )
                AsyncConnectionTask.RequestType.SAVE_FUEL_PRICE -> handleReceivedFuelPriceConfirm(
                    response.responseContent
                )
            }
        }
    }

    override fun processConnectionTimeout() {
        showToast(activity, activity.getString(R.string.connectionTimeout))
    }

    fun loadGasStationFuels(gasStationId: Long) {
        val requestBuilder =
            RequestBuilder(activity.getString(R.string.retrieveStationFuels))
        requestBuilder.putParameter("stationId", gasStationId.toString())
        connectionTaskFactory.create(this, AsyncConnectionTask.RequestType.RETRIEVE_FUELS)
            .execute(requestBuilder.build())
    }

    fun loadGasStationComments(gasStationId: Long) {
        val requestBuilder =
            RequestBuilder(activity.getString(R.string.retrieveStationComments))
        requestBuilder.putParameter("stationId", gasStationId.toString())
        connectionTaskFactory.create(this, AsyncConnectionTask.RequestType.RETRIEVE_COMMENTS)
            .execute(requestBuilder.build())
    }

    fun processConfirmStation(stationId: Long) {
        val appPreferences: AppPreferences? = AppPreferences.getInstance(activity)
        val requestBuilder =
            RequestBuilder(activity.getString(R.string.confirmStationUrl))
        requestBuilder.putParameter("stationId", java.lang.Long.toString(stationId))
        requestBuilder.putParameter(
            "userId",
            java.lang.String.valueOf(appPreferences!!.getLong(AppPreferences.Key.USER_ID))
        )
        connectionTaskFactory.create(this, AsyncConnectionTask.RequestType.CONFIRM_STATION)
            .execute(requestBuilder.build())
    }

    private fun handleReceivedGasStationFuels(responseContent: String) {
        try {
            val responseJson = JSONObject(responseContent)
            val status = responseJson.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                handleSuccessfulRetrieveGasStationsFuels(responseJson.getString(ResponseStatus.Key.CONTENT))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun handleReceivedGasStationComments(responseContent: String) {
        try {
            val responseJson = JSONObject(responseContent)
            val status = responseJson.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                handleSuccessfulRetrieveGasStationsComments(responseJson.getString(ResponseStatus.Key.CONTENT))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun handleSuccessfulRetrieveGasStationsFuels(responseContent: String) {
        val listType =
            object : TypeToken<List<Fuel?>?>() {}.type
        val fuels: List<Fuel> =
            Gson().fromJson<List<Fuel>>(responseContent, listType)
        fuelsAdapter.addFuels(fuels)
    }

    private fun handleSuccessfulRetrieveGasStationsComments(responseContent: String) {
        val listType =
            object : TypeToken<List<Comment?>?>() {}.type
        val comments: List<Comment> =
            Gson().fromJson<List<Comment>>(responseContent, listType)
        commentsAdapter.addComments(comments)
    }

    fun processCommentSubmitRequest(
        commentBody: String?,
        commentRate: String?,
        gasStationID: String?
    ) {
        sendCommentSubmitRequest(commentBody, commentRate, gasStationID)
    }

    private fun sendCommentSubmitRequest(
        commentBody: String?,
        commentRate: String?,
        gasStationID: String?
    ) {
        val appPreferences = AppPreferences.getInstance(activity)
        val requestBuilder =
            RequestBuilder(activity.getString(R.string.addCommentUrl))
        requestBuilder.putParameter(
            "user_id",
            java.lang.String.valueOf(appPreferences!!.getLong(AppPreferences.Key.USER_ID))
        )
        requestBuilder.putParameter("station_id", gasStationID)
        requestBuilder.putParameter("rating", commentRate)
        requestBuilder.putParameter("content", commentBody)
        val registerTask =
            connectionTaskFactory.create(this, AsyncConnectionTask.RequestType.SUBMIT_COMMENT)
        registerTask.execute(requestBuilder.build())
    }

    fun toggleFavouriteStatus() {
        Log.d("DUPA_SIZE", "Fav")
        val requestBuilder =
            RequestBuilder(activity.getString(R.string.toggleGasStationFavourite))
        requestBuilder.putParameter(
            "userId",
            UserDataHolder.getUserData(activity).id.toString() + ""
        )
        requestBuilder.putParameter("stationId", gasStation.id.toString() + "")
        connectionTaskFactory.create(this, AsyncConnectionTask.RequestType.TOGGLE_FAVOURITE_STATUS)
            .execute(requestBuilder.build())
    }

    fun processSaveFuelPriceRequest(
        stationId: Long,
        fuelId: Long,
        price: String?
    ) {
        val requestBuilder =
            RequestBuilder(activity.getString(R.string.fuelPriceUrl))
        requestBuilder.putParameter("stationId", java.lang.Long.toString(stationId))
        requestBuilder.putParameter("fuelId", java.lang.Long.toString(fuelId))
        requestBuilder.putParameter("price", price)
        connectionTaskFactory.create(this, AsyncConnectionTask.RequestType.SAVE_FUEL_PRICE)
            .execute(requestBuilder.build())
    }

    init {
        this.activity = activity
        this.connectionTaskFactory = taskFactory
        this.fuels = ArrayList<Fuel>()
        this.fuelsAdapter = GasStationFuelsAdapter(activity, fuels)
        this.activity.setGasStationFuelsAdapter(fuelsAdapter)
        this.comments = ArrayList<Comment>()
        this.commentsAdapter = GasStationCommentsAdapter(activity, comments, this)
        this.activity.setGasStationCommentsAdapter(commentsAdapter)
        this.gasStation = gasStation
    }

    override fun OnClick(view: View?, comment: Comment?) {
        showToast(view!!.context, "RemoveClick on " + comment!!.id)
        val intent: Intent = activity.getIntent()
        activity.finish()
        activity.startActivity(intent)
    }

    private fun handleCommentSubmitResponse(responseContent: String) {
        try {
            val responseJson = JSONObject(responseContent)
            val status = responseJson.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                val intent = activity.intent
                activity.finish()
                activity.startActivity(intent)
                showToast(activity, activity.getString(R.string.commentAdded))
            } else {
                showToast(activity, activity.getString(R.string.commentAddedFail))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun handleReceivedFuelPriceConfirm(responseContent: String) {
        try {
            val jsonResponse = JSONObject(responseContent)
            val status = jsonResponse.getInt(ResponseStatus.Key.STATUS)
            Log.d("myTag", jsonResponse.toString())
            val data = jsonResponse.getJSONObject(ResponseStatus.Key.CONTENT)
            if (status == ResponseStatus.General.SUCCESS) {
                showToast(activity, activity.getString(R.string.fuelPriceSaved))
                loadGasStationFuels(data.getInt("stationId").toLong())
            } else {
                showToast(activity, activity.getString(R.string.fuelPriceSaveError))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
