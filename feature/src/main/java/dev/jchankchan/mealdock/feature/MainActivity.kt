package dev.jchankchan.mealdock.feature

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val intent = Intent(this, FirebaseService::class.java)
        bindService(intent, firebaseServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            firebaseService.RC_SIGN_IN -> firebaseService.onResultAuth(resultCode, data)
        }
    }

    private lateinit var firebaseService : FirebaseService
    private val firebaseServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName,
                               binder: IBinder) {
            val bindService = binder as FirebaseService.FirebaseApiBinder
            firebaseService = bindService.service
            firebaseService.startSession(this@MainActivity)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {}
    }

}
