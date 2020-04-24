package com.example.fuelcomparison.source

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class AppPreferences private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences
    private var editor: Editor? = null
    fun getString(key: Key): String? {
        return sharedPreferences.getString(key.name, null)
    }

    fun getLong(key: Key): Long {
        return sharedPreferences.getLong(key.name, 0)
    }

    fun remove(vararg keys: Key) {
        edit()
        for (key in keys) {
            editor!!.remove(key.name)
        }
        commit()
    }

    fun put(
        key: Key,
        `val`: String?
    ) {
        edit()
        editor!!.putString(key.name, `val`)
        commit()
    }


    fun put(
        key: Key,
        `val`: Long
    ) {
        edit()
        editor!!.putLong(key.name, `val`)
        commit()
    }

    private fun edit() {
        if (editor == null) {
            editor = sharedPreferences.edit()
        }
    }

    private fun commit() {
        if (editor != null) {
            editor!!.commit()
            editor = null
        }
    }

    enum class Key {
        USER_ID, USER_NAME, USER_EMAIL, USER_TOKEN
    }

    companion object {
        const val SETTINGS_NAME = "app_settings"
        private var appPreferences: AppPreferences? = null

        @JvmStatic
        fun getInstance(context: Context): AppPreferences? {
            if (appPreferences == null) {
                appPreferences = AppPreferences(context)
            }
            return appPreferences
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(
            SETTINGS_NAME,
            Context.MODE_PRIVATE
        )
    }
}