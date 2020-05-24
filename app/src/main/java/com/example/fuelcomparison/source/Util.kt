package com.example.fuelcomparison.source

import android.content.Context
import android.widget.Toast

object Util {

    fun showToast(context: Context?, message: String?) {
        var message = message
        message = message ?: ""
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}