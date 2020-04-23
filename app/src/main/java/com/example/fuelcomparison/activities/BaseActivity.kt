package com.example.fuelcomparison.activities

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fuelcomparison.R

open class BaseActivity : AppCompatActivity() {
    protected var toolbar: Toolbar? = null
    protected var toolbarTitle: TextView? = null

    fun setupToolbar(toolbarId: Int, title: String?) {
        toolbar = findViewById(toolbarId)
        setSupportActionBar(toolbar)
        toolbarTitle = toolbar!!.findViewById(R.id.toolbarTitleTextView)
        toolbarTitle!!.text = title
        clearDefaultActionBarTitle()
    }

    fun clearDefaultActionBarTitle() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = ""
        }
    }
}