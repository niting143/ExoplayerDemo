package com.arraigntech.mobioticstask.ui.homeActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private var homeRepository = HomeRepository
    private var mutableData = MutableLiveData<List<HomeItem>>()
    val listLiveData : LiveData<List<HomeItem>> get() = mutableData

    init {
        viewModelScope.launch {
                init()
        }
    }

   private suspend fun init(){
        withContext(Dispatchers.Main){
            val response = homeRepository.api.getVideoList()
            if(response.isSuccessful){
                mutableData.value = response.body()
            }
        }

    }

}