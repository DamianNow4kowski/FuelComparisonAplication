package com.example.fuelcomparison.controllers

import com.example.fuelcomparison.R
import com.example.fuelcomparison.activities.RegisterActivity
import com.example.fuelcomparison.data.Response
import com.example.fuelcomparison.interfaces.AsyncConnectionCallback
import com.example.fuelcomparison.source.*
import com.example.fuelcomparison.source.Util.showToast
import org.json.JSONException
import org.json.JSONObject

class RegisterActivityController(
    private val activity: RegisterActivity, private val taskFactory: AsyncConnectionTaskFactory
) : AsyncConnectionCallback {

    override fun processRequestResponse(response: Response?) {
        activity.hideProgressIndicator()
        if (response!!.requestType == AsyncConnectionTask.RequestType.REGISTER) {
            handleRegisterResponse(
                response.responseContent
            )
        }
    }

    override fun processConnectionTimeout() {
        showToast(
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
            sendRegisterRequest(userEmail, userName, userPassword, false)
        }
    }

    private fun sendRegisterRequest(
        userEmail: String,
        userName: String,
        userPassword: String,
        userIsAgent: Boolean
    ) {
        val requestBuilder =
            RequestBuilder(activity.getString(R.string.registerUrl))
        requestBuilder.putParameter("email", userEmail)
        requestBuilder.putParameter("username", userName)
        requestBuilder.putParameter("password", userPassword)
        requestBuilder.putParameter("isAgent", java.lang.Boolean.toString(userIsAgent))
        val registerTask =
            taskFactory.create(this, AsyncConnectionTask.RequestType.REGISTER)
        registerTask.execute(requestBuilder.build())
    }


    private fun handleRegisterResponse(responseContent: String) {
        try {
            val jsonResponse = JSONObject(responseContent)
            val status = jsonResponse.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                handleSuccessfulRegistration()
            } else {
                handleUnsuccessfulRegistration(jsonResponse.getString(ResponseStatus.Key.REASON))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @Throws(JSONException::class)
    private fun handleSuccessfulRegistration() {
        showToast(
            activity,
            activity.getString(R.string.succesfulkRegistration)
        )
        activity.launchLoginActivity()
    }

    private fun handleUnsuccessfulRegistration(errorReason: String) {
        when (errorReason) {
            ResponseStatus.Register.NULL_OR_EMPTY_INPUT -> showToast(
                activity,
                activity.getString(R.string.emptyDataProvided)
            )
            ResponseStatus.Register.INCORRECT_DATA -> showToast(
                activity,
                activity.getString(R.string.registerDataIncorrect)
            )
            ResponseStatus.Register.USERNAME_OR_EMAIL_TAKEN -> showToast(
                activity,
                activity.getString(R.string.usernameOrEmailTaken)
            )
        }
    }

}