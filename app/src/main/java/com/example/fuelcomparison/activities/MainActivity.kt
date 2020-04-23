package com.example.fuelcomparison.activities

import android.os.Bundle
import android.view.MenuItem
import com.example.fuelcomparison.R
import com.example.fuelcomparison.controllers.MainActivityController

class MainActivity : BaseActivity() {
    private var controller: MainActivityController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_with_drawer)
        setupToolbar(R.id.toolbar, getString(R.string.app_name))
        controller = MainActivityController(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
