package com.example.fuelcomparison.controllers

import com.example.fuelcomparison.R
import com.example.fuelcomparison.activities.RegisterActivity
import com.example.fuelcomparison.data.Response
import com.example.fuelcomparison.interfaces.AsyncConnectionCallback
import com.example.fuelcomparison.source.*
import org.json.JSONException
import org.json.JSONObject

class RegisterActivityController(
    private val activity: RegisterActivity, private val taskFactory: AsyncConnectionTaskFactory
) : AsyncConnectionCallback {
    override fun processRequestResponse(response: Response) {
        activity.hideProgressIndicator()
        if (response.requestType == AsyncConnectionTask.RequestType.REGISTER) handleRegisterResponse(
            response.responseContent
        )
    }

    override fun processConnectionTimeout() {
        Util.showToast(
            activity,
            activity.getString(R.string.connectionTimeout)
        )
        activity.hideProgressIndicator()
    }

    fun processRegisterRequest(
        userEmail: String,
        userName: String,
        userPassword: String,
        userConfirmPassword: String
    ) {
        if (userPassword != userConfirmPassword) {
            Util.showToast(
                activity,
                activity.getString(R.string.passwordsDontMatch)
            )
            activity.hideProgressIndicator()
        } else {
            sendRegisterRequest(userEmail, userName, userPassword)
        }
    }

    private fun sendRegisterRequest(
        userEmail: String,
        userName: String,
        userPassword: String
    ) {
        val requestBuilder =
            RequestBuilder(activity.getString(R.string.registerUrl))
        requestBuilder.putParameter("email", userEmail)
        requestBuilder.putParameter("username", userName)
        requestBuilder.putParameter("password", userPassword)
        val registerTask =
            taskFactory.create(this, AsyncConnectionTask.RequestType.REGISTER)
        registerTask.execute(requestBuilder.build())
    }

    private fun handleRegisterResponse(responseContent: String) {
        try {
            val jsonResponse = JSONObject(responseContent)
            val status =
                jsonResponse.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                handleSuccessfulRegistration(jsonResponse.getJSONObject(ResponseStatus.Key.CONTENT))
            } else {
                handleUnsuccessfulRegistration(jsonResponse.getInt(ResponseStatus.Key.ERROR_CODE))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @Throws(JSONException::class)
    private fun handleSuccessfulRegistration(userData: JSONObject) {
        activity.launchLoginActivity()
    }

    private fun handleUnsuccessfulRegistration(errorCode: Int) {
        when (errorCode) {
            ResponseStatus.Register.NULL_OR_EMPTY_INPUT -> Util.showToast(
                activity,
                activity.getString(R.string.emptyDataProvided)
            )
            ResponseStatus.Register.INCORRECT_DATA -> Util.showToast(
                activity,
                activity.getString(R.string.registerDataIncorrect)
            )
            ResponseStatus.Register.USERNAME_OR_EMAIL_TAKEN -> Util.showToast(
                activity,
                activity.getString(R.string.usernameOrEmailTaken)
            )
        }
    }

}