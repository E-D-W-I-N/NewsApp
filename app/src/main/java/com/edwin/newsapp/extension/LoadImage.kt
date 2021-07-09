package com.edwin.newsapp.extension

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(context: Context, imageId: String?) {
    Glide.with(context)
        .load(imageId)
        .placeholder(ColorDrawable(Color.WHITE))
        .into(this)
}