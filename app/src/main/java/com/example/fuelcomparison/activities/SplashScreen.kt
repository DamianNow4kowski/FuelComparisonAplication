package com.example.fuelcomparison.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fuelcomparison.source.UserDataHolder

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isUserLoggedIn) {
            launchMainActivity()
        } else {
            launchLoginActivity()
        }
    }

    private val isUserLoggedIn: Boolean
        get() = UserDataHolder.getUserData(this).token != null

    private fun launchMainActivity() {
        startActivityClear(MainActivity::class.java)
    }

    private fun launchLoginActivity() {
        startActivityClear(LoginActivity::class.java)
    }

    private fun <T> startActivityClear(newActivityClass: Class<T>) {
        val intent = Intent(this, newActivityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}