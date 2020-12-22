package com.arraigntech.mobioticstask.utils

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

public class Constants{
    companion object{

        const val RC_SIGN_IN = 123
        const val USER = "user"
        const val USERS = "users"
        const val SHAREDPREF_FILE = "sharedPrefFile"
        const val IS_LOGGEDIN = "isLogin"

        //Url
        const val BASE_URL = "https://interview-e18de.firebaseio.com/"

        //intentStr
        const val LIST_STR = "list"




        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ShapeableImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                //Picasso.get().load(url).into(view)
                Glide.with(view.context).load(url).into(view)
            }
        }

    }
}