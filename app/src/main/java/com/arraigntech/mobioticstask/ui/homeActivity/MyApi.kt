package com.arraigntech.mobioticstask.ui.homeActivity

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface MyApi {

    @GET("media.json?print=pretty")
    suspend fun getVideoList() : Response<List<HomeItem>>

}