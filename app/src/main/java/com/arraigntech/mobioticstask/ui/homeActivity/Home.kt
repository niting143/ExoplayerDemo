package com.arraigntech.mobioticstask.ui.homeActivity

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.annotations.SerializedName

class Home : ArrayList<HomeItem>()
data class HomeItem(
    val description: String,
    val id: String,
    val thumb: String,
    val title: String,
    val url: String
)