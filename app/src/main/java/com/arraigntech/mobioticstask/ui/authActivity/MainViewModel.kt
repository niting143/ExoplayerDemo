package com.arraigntech.mobioticstask.ui.authActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential

class MainViewModel : ViewModel(){
    private lateinit var authRepository : AuthRepository
    lateinit var authenticatedUserLiveData : LiveData<User>
    lateinit var creaatedUserLiveData : LiveData<User>

    fun init(){
        authRepository = AuthRepository()
    }

    fun signInWithGoogle(authCredential: AuthCredential){
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(authCredential)
    }

    fun createUser(authenticatedUser : User){
        creaatedUserLiveData = authRepository.createUserInFirestoreIfNotExists(authenticatedUser)
    }
}