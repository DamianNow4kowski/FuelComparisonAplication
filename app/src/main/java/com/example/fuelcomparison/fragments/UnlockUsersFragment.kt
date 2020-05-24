package com.example.fuelcomparison.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelcomparison.R
import com.example.fuelcomparison.adapters.ActiveUsersAdapter
import com.example.fuelcomparison.adapters.BlockedUsersAdapter
import com.example.fuelcomparison.controllers.UnlockUsersFragmentController
import com.example.fuelcomparison.source.AsyncConnectionTaskFactory

class UnlockUsersFragment : Fragment() {
    private var controller: UnlockUsersFragmentController? = null
    private var agentIdArea: TextView? = null
    private var blockedUsers: RecyclerView? = null
    private var activeUsers: RecyclerView? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View =
            inflater.inflate(R.layout.fragment_unlock_users, container, false)
        agentIdArea = view.findViewById(R.id.agentId)
        val sendButton =
            view.findViewById<Button>(R.id.unlockUserButton)
        sendButton.setOnClickListener(createUnlockUserClickListener())
        blockedUsers = view.findViewById(R.id.blockedUsers)
        blockedUsers!!.layoutManager = LinearLayoutManager(activity)
        activeUsers = view.findViewById(R.id.activeUsers)
        activeUsers!!.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onStart() {
        super.onStart()
        controller =
            UnlockUsersFragmentController(this, this.requireActivity(), AsyncConnectionTaskFactory())
        controller!!.retrieveAllUsers()
    }

    fun setActiveUsersAdapter(adapter: ActiveUsersAdapter?) {
        activeUsers!!.adapter = adapter
    }

    fun setBlockedUsersAdapter(adapter: BlockedUsersAdapter?) {
        blockedUsers!!.adapter = adapter
    }

    /**
     * Only required for UT DI.
     * Should not be used in production code.
     */
    @Deprecated("")
    fun setController(controller: UnlockUsersFragmentController?) {
        this.controller = controller
    }

    private fun createUnlockUserClickListener(): View.OnClickListener {
        return View.OnClickListener { v: View? ->
            val agentId = agentIdArea!!.text.toString().trim { it <= ' ' }
            controller!!.processUnlockUserSend(agentId)
        }
    }
}