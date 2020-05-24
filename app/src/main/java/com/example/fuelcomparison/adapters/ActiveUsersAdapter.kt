package com.example.fuelcomparison.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelcomparison.R
import com.example.fuelcomparison.activities.MainActivity
import com.example.fuelcomparison.data.UserData
import com.example.fuelcomparison.source.Util.showToast
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

class ActiveUsersAdapter(private val context: Context) :
    RecyclerView.Adapter<ActiveUsersAdapter.ViewHolder?>() {
    private val activeUsers: MutableList<UserData>

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        @NonNull holder: ViewHolder,
        position: Int
    ) {
        val user: UserData = activeUsers[position]
        holder.userId.text = (user.id).toString()
        holder.userName.text = user.name
        holder.userEmail.text = user.email
    }

    fun clear() {
        activeUsers.clear()
        notifyDataSetChanged()
    }

    fun addUsers(allUsers: List<UserData>) {
        val filteredUsers: List<UserData> = allUsers.stream()
            .filter(Predicate<UserData> { user: UserData -> user.active })
            .collect(Collectors.toList<Any>()) as List<UserData>
        activeUsers.addAll(filteredUsers)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val userId: TextView
        val userName: TextView
        val userEmail: TextView
        private val userActive: TextView? = null
        override fun onClick(v: View) {
            val activity: MainActivity = context as MainActivity
            showToast(activity, "click")
        }

        init {
            userId = itemView.findViewById(R.id.userId)
            userName = itemView.findViewById(R.id.userName)
            userEmail = itemView.findViewById(R.id.userEmail)
            itemView.setOnClickListener(this)
        }
    }

    init {
        activeUsers = ArrayList<UserData>()
    }

    override fun getItemCount(): Int {
        return activeUsers.size
    }
}