package dev.jchankchan.mealdock.feature

import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dmax.dialog.SpotsDialog

class FirebaseService : Service() {

    private val firebaseApiBinder = FirebaseApiBinder()
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog : AlertDialog

    override fun onCreate() {
        super.onCreate()
        auth = FirebaseAuth.getInstance()
    }

    override fun onBind(intent: Intent): IBinder? {
        return firebaseApiBinder
    }

    inner class FirebaseApiBinder : Binder() {
        val service: FirebaseService
            get() = this@FirebaseService
    }

    val TAG = FirebaseService::class.java.simpleName
    val RC_SIGN_IN = 9999
    fun requestToAuth(activity: Activity) {
        // Create and launch sign-in intent

        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    private val providers : List<AuthUI.IdpConfig>
        get() {
            // Choose authentication providers
            return arrayListOf(
                    //AuthUI.IdpConfig.GoogleBuilder().build(),
                    //AuthUI.IdpConfig.PhoneBuilder().build(),
                    //AuthUI.IdpConfig.TwitterBuilder().build(),
                    //AuthUI.IdpConfig.FacebookBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build())
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
        dialog = SpotsDialog.Builder().setContext(activity).build()
        dialog.show()

        currentUser = auth.currentUser
        if (currentUser == null) {
            //signInWith(activity, "member_unittest@mealdock.com", "unittest")
            silentSignIn(activity)
        } else {
            dialog.dismiss()
        }
    }

    fun signInWith(activity: Activity, email: String, password: String) {
        //var config = AuthUI.IdpConfig
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    onCompletionAuthResult(activity, task)
                }
    }

    fun silentSignIn(activity: Activity) {
        dialog.show()
        AuthUI.getInstance()
                .silentSignIn(activity, providers)
                .addOnCompleteListener(activity) { task ->
                    onCompletionAuthResult(activity, task)
                }
    }

    private fun onCompletionAuthResult(activity: Activity, task : Task<AuthResult>) {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithEmail:success")
            currentUser = auth.currentUser
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.exception)
            Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            requestToAuth(activity)
        }
    }

    fun signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    // ...
                }
    }

    fun deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener {
                    // ...
                }
    }
}
