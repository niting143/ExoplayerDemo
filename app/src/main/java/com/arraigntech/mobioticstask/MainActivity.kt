package com.arraigntech.mobioticstask

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arraigntech.mobioticstask.databinding.ActivityMainBinding
import com.arraigntech.mobioticstask.ui.authActivity.MainViewModel
import com.arraigntech.mobioticstask.ui.authActivity.User
import com.arraigntech.mobioticstask.utils.Constants.Companion.IS_LOGGEDIN
import com.arraigntech.mobioticstask.utils.Constants.Companion.RC_SIGN_IN
import com.arraigntech.mobioticstask.utils.Constants.Companion.SHAREDPREF_FILE
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignIn: GoogleSignInClient
    private lateinit var mainViewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        sharedPreferences = this.getSharedPreferences(
            SHAREDPREF_FILE,
            Context.MODE_PRIVATE
        )
        if (sharedPreferences.getBoolean(IS_LOGGEDIN,false)) {
            gotoMainActivity()
        }else {
            mainViewModel.init()
            initGoogleSignInClient()
            binding.gLogin.setOnClickListener {
                signIN()
            }
        }
    }

    fun signIN() {
        val signInIntent = googleSignIn.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun initGoogleSignInClient() {
        val googleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.defulat_web_client))
            .requestEmail().build()
        googleSignIn = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val gSignInAccount = task.getResult(ApiException::class.java)
                if (googleSignIn != null) {
                    getGoogleAuthCredential(gSignInAccount)
                }
            } catch (e: ApiException) {
                Log.e("", "" + e.message)
            }
        }
    }

    fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount?) {
        val googleTokenId = googleSignInAccount?.idToken
        val gAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(gAuthCredential)
    }

    fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        mainViewModel.signInWithGoogle(googleAuthCredential)
        mainViewModel.authenticatedUserLiveData.observe(this, Observer { it ->
            if (it.isNew) {
                createNewUser(it)
            } else {
                //gotoMainActivity(it)
                gotoMainActivity()
            }
        })
    }

    fun createNewUser(authencticateUser: User) {
        mainViewModel.createUser(authencticateUser)
        mainViewModel.creaatedUserLiveData.observe(this, Observer { it ->
            if (it.isCreated) {
                toastMessage(it.name)
            } else {
                //gotoMainActivity(it) //passing user details as arguement
                gotoMainActivity()
            }
        })
    }

    fun toastMessage(mesage: String) {
        Toast.makeText(this, mesage, Toast.LENGTH_SHORT).show()
    }

    fun gotoMainActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(IS_LOGGEDIN, true)
        editor.apply()
        editor.commit()
        finish()
    }


}