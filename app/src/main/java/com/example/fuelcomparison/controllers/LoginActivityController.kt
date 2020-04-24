package com.example.fuelcomparison.controllers

import com.example.fuelcomparison.R
import com.example.fuelcomparison.activities.LoginActivity
import com.example.fuelcomparison.data.Response
import com.example.fuelcomparison.interfaces.AsyncConnectionCallback
import com.example.fuelcomparison.source.*
import com.example.fuelcomparison.source.Util.showToast
import org.json.JSONException
import org.json.JSONObject

class LoginActivityController(
    private val activity: LoginActivity, private val taskFactory: AsyncConnectionTaskFactory
) : AsyncConnectionCallback {

    fun processLoginRequest(
        userLogin: String?,
        userPassword: String?
    ) {
        val requestBuilder = RequestBuilder(activity.getString(R.string.loginUrl))
        requestBuilder.putParameter("email", userLogin)
        requestBuilder.putParameter("password", userPassword)
        val loginTask =
            taskFactory!!.create(this, AsyncConnectionTask.RequestType.LOGIN)
        loginTask.execute(requestBuilder.build())
    }


    override fun processConnectionTimeout() {
        showToast(activity, activity.getString(R.string.connectionTimeout))
        activity.hideProgressIndicator()
    }

    override fun processRequestResponse(response: Response) {
        activity.hideProgressIndicator()
        if (response.requestType == AsyncConnectionTask.RequestType.LOGIN) handleLoginResponse(
            response.responseContent
        )
    }

    private fun handleLoginResponse(responseContent: String) {
        try {
            val jsonResponse = JSONObject(responseContent)
            val status = jsonResponse.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                handleSuccessfulLogin(jsonResponse.getJSONObject(ResponseStatus.Key.CONTENT))
            } else {
                handleUnsuccessfulLogin(jsonResponse.getInt(ResponseStatus.Key.ERROR_CODE))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @Throws(JSONException::class)
    private fun handleSuccessfulLogin(userData: JSONObject) {
        val appPreferences: AppPreferences = AppPreferences.getInstance(activity)!!
        appPreferences.put(AppPreferences.Key.USER_ID, userData.getLong("id"))
        appPreferences.put(AppPreferences.Key.USER_NAME, userData.getString("name"))
        appPreferences.put(AppPreferences.Key.USER_EMAIL, userData.getString("email"))
        appPreferences.put(AppPreferences.Key.USER_TOKEN, userData.getString("token"))
        activity.launchMainActivity()
    }

    private fun handleUnsuccessfulLogin(errorCode: Int) {
        when (errorCode) {
            ResponseStatus.Login.NULL_OR_EMPTY_INPUT -> showToast(
                activity,
                activity.getString(R.string.emptyDataProvided)
            )
            ResponseStatus.Login.INCORRECT_DATA -> showToast(
                activity,
                activity.getString(R.string.loginDataIncorrect)
            )
        }
    }
}