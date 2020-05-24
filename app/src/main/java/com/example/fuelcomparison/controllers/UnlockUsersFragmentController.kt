package com.example.fuelcomparison.controllers

import androidx.fragment.app.FragmentActivity
import com.example.fuelcomparison.R
import com.example.fuelcomparison.adapters.ActiveUsersAdapter
import com.example.fuelcomparison.adapters.BlockedUsersAdapter
import com.example.fuelcomparison.data.Response
import com.example.fuelcomparison.data.UserData
import com.example.fuelcomparison.fragments.UnlockUsersFragment
import com.example.fuelcomparison.interfaces.AsyncConnectionCallback
import com.example.fuelcomparison.source.AsyncConnectionTask
import com.example.fuelcomparison.source.AsyncConnectionTaskFactory
import com.example.fuelcomparison.source.RequestBuilder
import com.example.fuelcomparison.source.ResponseStatus
import com.example.fuelcomparison.source.Util.showToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject

class UnlockUsersFragmentController(
    fragment: UnlockUsersFragment,
    activity: FragmentActivity,
    factory: AsyncConnectionTaskFactory
) : AsyncConnectionCallback {
    private val fragment: UnlockUsersFragment
    private val activity: FragmentActivity
    private val taskFactory: AsyncConnectionTaskFactory
    private val activeUsersAdapter: ActiveUsersAdapter
    private val blockedUsersAdapter: BlockedUsersAdapter
    override fun processRequestResponse(response: Response?) {
        when (response!!.requestType) {
            AsyncConnectionTask.RequestType.UNLOCK_USER -> handleUnlockUserResponse(response.responseContent)
            AsyncConnectionTask.RequestType.RETRIEVE_ALL_USERS -> handleAllUsersResponse(response.responseContent)
        }
    }

    override fun processConnectionTimeout() {
        showToast(activity, activity.getString(R.string.connectionTimeout))
    }

    fun processUnlockUserSend(userId: String?) {
        val requestBuilder =
            RequestBuilder(activity.getString(R.string.unlockUserUrl))
        requestBuilder.putParameter("userId", userId)
        val loginTask: AsyncConnectionTask =
            taskFactory.create(this, AsyncConnectionTask.RequestType.UNLOCK_USER)
        loginTask.execute(requestBuilder.build())
    }

    fun retrieveAllUsers() {
        val requestBuilder =
            RequestBuilder(fragment.getString(R.string.retrieveAllUsers))
        taskFactory.create(this, AsyncConnectionTask.RequestType.RETRIEVE_ALL_USERS)
            .execute(requestBuilder.build())
    }

    private fun handleUnlockUserResponse(responseContent: String) {
        try {
            val jsonResponse = JSONObject(responseContent)
            val status = jsonResponse.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                handleSuccessfulUnlockUserResponse(jsonResponse.getJSONObject(ResponseStatus.Key.CONTENT))
            } else {
                handleUnsuccessfulUnlockUserResponse(jsonResponse.getInt(ResponseStatus.Key.ERROR_CODE))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @Throws(JSONException::class)
    private fun handleSuccessfulUnlockUserResponse(userData: JSONObject) {
        if (userData.getInt("active") == 1) {
            showToast(activity, activity.getString(R.string.userUnlocked))
        } else {
            showToast(activity, activity.getString(R.string.userBlocked))
        }
        retrieveAllUsers()
    }

    private fun handleUnsuccessfulUnlockUserResponse(errorCode: Int) {
        when (errorCode) {
            ResponseStatus.UnlockUser.INCORRECT_DATA -> showToast(
                activity,
                activity.getString(R.string.unlockUserDataIncorrect)
            )
        }
    }

    private fun handleAllUsersResponse(responseContent: String) {
        try {
            val jsonResponse = JSONObject(responseContent)
            val status = jsonResponse.getInt(ResponseStatus.Key.STATUS)
            if (status == ResponseStatus.General.SUCCESS) {
                handleSuccessfulAllUsersResponse(jsonResponse.getString(ResponseStatus.Key.CONTENT))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun handleSuccessfulAllUsersResponse(responseContent: String) {
        val listType =
            object : TypeToken<List<UserData?>?>() {}.type
        val users: List<UserData> =
            Gson().fromJson<List<UserData>>(responseContent, listType)
        activeUsersAdapter.clear()
        activeUsersAdapter.addUsers(users)
        blockedUsersAdapter.clear()
        blockedUsersAdapter.addUsers(users)
    }

    init {
        this.fragment = fragment
        this.activity = activity
        taskFactory = factory
        activeUsersAdapter = ActiveUsersAdapter(fragment.requireActivity())
        blockedUsersAdapter = BlockedUsersAdapter(fragment.requireActivity())
        fragment.setActiveUsersAdapter(activeUsersAdapter)
        fragment.setBlockedUsersAdapter(blockedUsersAdapter)
    }
}
