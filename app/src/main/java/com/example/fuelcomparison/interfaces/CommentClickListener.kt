package com.example.fuelcomparison.interfaces

import android.view.View
import com.example.fuelcomparison.data.Comment

interface CommentClickListener {
    fun OnClick(view: View?, comment: Comment?)
}
