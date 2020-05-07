package com.example.fuelcomparison.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.fuelcomparison.R
import com.example.fuelcomparison.controllers.MainActivityController
import com.example.fuelcomparison.data.GasStation
import com.example.fuelcomparison.enums.IntentKey
import com.example.fuelcomparison.fragments.FavouriteStationsFragment
import com.example.fuelcomparison.fragments.MapFragment
import com.example.fuelcomparison.source.AppPreferences
import com.example.fuelcomparison.source.UserDataHolder
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var controller: MainActivityController? = null
    private var drawerLayout: DrawerLayout? = null
    private var topNavigationView: NavigationView? = null
    private var bottomNavigationView: NavigationView? = null
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_with_drawer)
        setupToolbar(R.id.toolbar, getString(R.string.app_name))
        controller = MainActivityController(this)

        initializeDrawerLayout()
        initializeNavigationView()
        displayBasicUserDataInNavView()
        setMapFragment()
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

    fun startGasStationInfoActivity(gasStation: GasStation?) {
        val intent = Intent(this, GasStationInfoActivity::class.java)
        intent.putExtra(IntentKey.GAS_STATION.name, gasStation)
        startActivity(intent)
    }

    private fun initializeDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun initializeNavigationView() {
        topNavigationView = findViewById(R.id.navigation_view_top)
        topNavigationView!!.itemIconTintList = null
        topNavigationView!!.setNavigationItemSelectedListener(this)
        bottomNavigationView = findViewById(R.id.navigation_view_bottom)
        bottomNavigationView!!.itemIconTintList = null
        bottomNavigationView!!.setNavigationItemSelectedListener(this)
    }

    private fun setFavouriteStationsFragment() {
        activeFragment = FavouriteStationsFragment()
        var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction = fragmentTransaction.replace(
            R.id.frameLayout,
            activeFragment!!,
            getString(R.string.favStationsFragmentTag)
        )
        fragmentTransaction = fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun displayBasicUserDataInNavView() {
        val navHeaderView = topNavigationView!!.getHeaderView(0)
        val usernameLabel = navHeaderView.findViewById<TextView>(R.id.userNameLabel)
        val userEmailLabel = navHeaderView.findViewById<TextView>(R.id.userEmailLabel)
        val appPref: AppPreferences = AppPreferences.getInstance(this)!!
        usernameLabel.text = appPref.getString(AppPreferences.Key.USER_NAME)
        userEmailLabel.text = appPref.getString(AppPreferences.Key.USER_EMAIL)
    }

    private fun setMapFragment() {
        activeFragment = MapFragment()
        var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction = fragmentTransaction.replace(
            R.id.frameLayout,
            activeFragment as MapFragment,
            getString(R.string.mapFragmentTag)
        )
        fragmentTransaction = fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.navMapFragment -> setMapFragment()
            R.id.navFavStations -> setFavouriteStationsFragment()
            R.id.navLogout -> logout()
        }
        drawerLayout!!.closeDrawer(Gravity.START)
        return true
    }

    private fun logout() {
        UserDataHolder.removeUserData(this)
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
