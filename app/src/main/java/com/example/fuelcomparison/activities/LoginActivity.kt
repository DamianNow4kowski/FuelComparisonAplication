package com.example.fuelcomparison.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.annotation.Nullable
import com.example.fuelcomparison.R
import com.example.fuelcomparison.controllers.LoginActivityController
import com.example.fuelcomparison.source.AsyncConnectionTaskFactory

class LoginActivity : BaseActivity() {
    private var emailArea: EditText? = null
    private var passwordArea: EditText? = null
    protected var controller: LoginActivityController? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        controller = LoginActivityController(
            this,
            AsyncConnectionTaskFactory()
        )
        emailArea = findViewById(R.id.emailArea)
        passwordArea = findViewById(R.id.passwordArea)
    }

    fun handleRegisterLabelClick(view: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun handleLoginButtonClick(view: View?) {
        val userLogin = emailArea!!.text.toString().trim { it <= ' ' }
        val userPassword = passwordArea!!.text.toString().trim { it <= ' ' }
        showProgressIndicator()
        controller!!.processLoginRequest(userLogin, userPassword)
    }

    fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}