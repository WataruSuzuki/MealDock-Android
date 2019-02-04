package dev.jchankchan.mealdock.feature

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class FirebaseService : Service() {

    private val firebaseApiBinder = FirebaseApiBinder()

    override fun onBind(intent: Intent): IBinder? {
        return firebaseApiBinder
    }

    inner class FirebaseApiBinder : Binder() {
        val service: FirebaseService
            get() = this@FirebaseService
    }


}
