package com.example.fuelcomparison.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelcomparison.R
import com.example.fuelcomparison.activities.MainActivity
import com.example.fuelcomparison.adapters.FavouriteStationsAdapter
import com.example.fuelcomparison.controllers.FavouriteStationsFragmentController
import com.example.fuelcomparison.source.AsyncConnectionTaskFactory

class FavouriteStationsFragment : Fragment() {
    private var controller: FavouriteStationsFragmentController? = null
    private var gasStations: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity?
        mainActivity?.changeTitle(R.id.toolbar, getString(R.string.favouriteStations))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view =
            inflater.inflate(R.layout.fragment_favourite_stations, container, false)
        gasStations = view.findViewById(R.id.favouriteStations)
        gasStations!!.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onStart() {
        super.onStart()
        controller = FavouriteStationsFragmentController(this, AsyncConnectionTaskFactory())
        controller!!.retrieveFavouriteGasStations()
    }

    fun setAdapter(adapter: FavouriteStationsAdapter?) {
        gasStations!!.adapter = adapter
    }
}