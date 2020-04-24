package com.example.fuelcomparison.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.fuelcomparison.R
import com.example.fuelcomparison.controllers.RegisterActivityController
import com.example.fuelcomparison.source.AsyncConnectionTaskFactory

class RegisterActivity : BaseActivity() {
    private var emailArea: EditText? = null
    private var usernameArea: EditText? = null
    private var passwordArea: EditText? = null
    private var confirmPasswordArea: EditText? = null
    protected var controller: RegisterActivityController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        controller = RegisterActivityController(
            this,
            AsyncConnectionTaskFactory()
        )
        emailArea = findViewById(R.id.emailArea)
        usernameArea = findViewById(R.id.usernameArea)
        passwordArea = findViewById(R.id.passwordArea)
        confirmPasswordArea = findViewById(R.id.confirmPasswordArea)
    }

    fun handleLoginLabelClick(view: View?) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun handleRegisterButtonClick(view: View?) {
        val userEmail = emailArea!!.text.toString().trim { it <= ' ' }
        val userName = usernameArea!!.text.toString().trim { it <= ' ' }
        val userPassword = passwordArea!!.text.toString().trim { it <= ' ' }
        val userConfirmPassword =
            confirmPasswordArea!!.text.toString().trim { it <= ' ' }
        controller!!.processRegisterRequest(userEmail, userName, userPassword, userConfirmPassword)
    }

    fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}