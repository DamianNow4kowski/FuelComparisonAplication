package com.example.fuelcomparison.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import com.example.fuelcomparison.R
import com.example.fuelcomparison.controllers.LoginActivityController

class LoginActivity : BaseActivity() {
    private var emailArea: EditText? = null
    private var passwordArea: EditText? = null
    protected var controller: LoginActivityController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //        controller
        emailArea = findViewById(R.id.emailArea)
        passwordArea = findViewById(R.id.passwordArea)
    }

    fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}