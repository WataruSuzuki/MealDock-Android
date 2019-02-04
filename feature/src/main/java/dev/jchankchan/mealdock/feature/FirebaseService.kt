package dev.jchankchan.mealdock.feature

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseService : Service() {

    private val firebaseApiBinder = FirebaseApiBinder()

    override fun onBind(intent: Intent): IBinder? {
        return firebaseApiBinder
    }

    inner class FirebaseApiBinder : Binder() {
        val service: FirebaseService
            get() = this@FirebaseService
    }

    val RC_SIGN_IN = 9999
    fun requestToAuth(activity: Activity) {
        // Choose authentication providers
        val providers = arrayListOf(
                //AuthUI.IdpConfig.GoogleBuilder().build(),
                //AuthUI.IdpConfig.PhoneBuilder().build(),
                //AuthUI.IdpConfig.TwitterBuilder().build(),
                //AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build())
        // Create and launch sign-in intent

        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    fun onResultAuth(resultCode: Int, data: Intent?) {
        val response = IdpResponse.fromResultIntent(data)

        if (resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            currentUser = FirebaseAuth.getInstance().currentUser
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    private var currentUser : FirebaseUser? = null
    fun startSession(activity: Activity) {
        if (currentUser == null) {
            requestToAuth(activity)
        }
    }

}
