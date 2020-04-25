package com.example.fuelcomparison.activities

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fuelcomparison.R

open class BaseActivity : AppCompatActivity() {
    protected var toolbar: Toolbar? = null
    private var toolbarTitle: TextView? = null

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

    open fun showProgressIndicator() {
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
    }

    open fun hideProgressIndicator() {
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
    }
}