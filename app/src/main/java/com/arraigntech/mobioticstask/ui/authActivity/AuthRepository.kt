package com.arraigntech.mobioticstask.ui.authActivity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.arraigntech.mobioticstask.utils.Constants.Companion.USERS
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val userRef = rootRef.collection(USERS)

    fun firebaseSignInWithGoogle(authCredential: AuthCredential): MutableLiveData<User> {
        val authenticatedUserMutableLiveData = MutableLiveData<User>();
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val isNewUser = task.result!!.additionalUserInfo!!.isNewUser
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser != null) {
                    val uId = firebaseUser.uid
                    val name = firebaseUser.displayName
                    val email = firebaseUser.email
                    val user = User(
                        uId,
                        name!!,
                        email!!
                    )
                    user.isNew = isNewUser
                    authenticatedUserMutableLiveData.value = user
                }
            } else {
                Log.e("", "" + task.exception!!.message)
            }
        }
        return authenticatedUserMutableLiveData
    }


    fun createUserInFirestoreIfNotExists(authenticatedUser: User): MutableLiveData<User> {
        val newUserMutableLiveData = MutableLiveData<User>()
        val uidRef = userRef.document(authenticatedUser.userId)
        uidRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (!document!!.exists()) {
                    uidRef.set(authenticatedUser).addOnCompleteListener { userCreationTask ->
                        if (userCreationTask.isSuccessful) {
                            authenticatedUser.isCreated = true
                            newUserMutableLiveData.value = authenticatedUser
                        } else {
                            Log.e("", "" + userCreationTask.exception!!.message)
                        }
                    }
                } else {
                    newUserMutableLiveData.value = authenticatedUser
                }
            } else {
                Log.e("", "" + task.exception!!.message)
            }
        }

        return newUserMutableLiveData
    }

}