package com.example.fuelcomparison.source

import android.content.Context
import com.example.fuelcomparison.data.UserData
import com.example.fuelcomparison.source.AppPreferences
import com.example.fuelcomparison.source.AppPreferences.Companion.getInstance

class UserDataHolder private constructor() {
    companion object {
        private lateinit var userData: UserData
        fun getUserData(context: Context): UserData {
            initializeUserData(context)
            return userData
        }

        fun removeUserData(context: Context) {
            clearUserData(context)
        }

        private fun initializeUserData(context: Context) {
            val appPreferences = getInstance(context)
            userData = UserData()
            userData.id =
                appPreferences!!.getLong(AppPreferences.Key.USER_ID)
            userData.name =
                appPreferences.getString(AppPreferences.Key.USER_NAME)
            userData.email =
                appPreferences.getString(AppPreferences.Key.USER_EMAIL)
            userData.token =
                appPreferences.getString(AppPreferences.Key.USER_TOKEN)
        }

        private fun clearUserData(context: Context) {
            val appPreferences = getInstance(context)
            userData = UserData()
            appPreferences!!.remove(
                AppPreferences.Key.USER_ID,
                AppPreferences.Key.USER_EMAIL,
                AppPreferences.Key.USER_NAME,
                AppPreferences.Key.USER_TOKEN
            )
        }
    }

    init {
        userData = UserData()
    }
}