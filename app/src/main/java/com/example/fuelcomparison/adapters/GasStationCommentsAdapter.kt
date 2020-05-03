package com.example.fuelcomparison.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelcomparison.R
import com.example.fuelcomparison.controllers.GasStationInfoController
import com.example.fuelcomparison.data.Comment
import com.example.fuelcomparison.interfaces.CommentClickListener
import com.example.fuelcomparison.source.AppPreferences

class GasStationCommentsAdapter(
    @param:NonNull private val context: Context,
    comments: MutableList<Comment>,
    listener: GasStationInfoController
) : RecyclerView.Adapter<GasStationCommentsAdapter.ViewHolder?>() {
    private val comments: MutableList<Comment>

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        @NonNull holder: ViewHolder,
        position: Int
    ) {
        val comment: Comment = comments[position]
        holder.commentContent.text = comment.content
        holder.gasStationRate.text = comment.rating.toString()
    }

    fun addComments(comments: List<Comment>?) {
        this.comments.addAll(comments!!)
        notifyDataSetChanged()
    }

    private fun getCommentAt(possition: Int): Comment {
        return comments[possition]
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnLongClickListener {
        val commentContent: TextView
        val gasStationRate: TextView
        override fun onLongClick(v: View): Boolean {
            val appPreferences: AppPreferences = AppPreferences.getInstance(context)!!
            if (appPreferences.getBoolean(AppPreferences.Key.USER_ADMIN)) {
                commentClickListener.OnClick(
                    v,
                    getCommentAt(this.getLayoutPosition())
                )
            }
            return true
        }

        init {
            commentContent = itemView.findViewById(R.id.commentContent)
            gasStationRate = itemView.findViewById(R.id.gasStationRate)
            itemView.setOnLongClickListener(this)
        }
    }

    companion object {
        private lateinit var commentClickListener: CommentClickListener
    }

    init {
        this.comments = comments
        commentClickListener = listener
    }

    override fun getItemCount(): Int {
        return comments.size
    }

}