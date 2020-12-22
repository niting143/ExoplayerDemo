package com.arraigntech.mobioticstask.ui.detailpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel(){
    private var mut = MutableLiveData<Boolean>()
    val livData : LiveData<Boolean> get() = mut
    fun onIMGClick(){
        mut.value = true
    }

}