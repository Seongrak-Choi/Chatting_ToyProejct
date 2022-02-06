package com.example.chatting_toyproject

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class SplashActivity : AppCompatActivity() {

    private lateinit var rootLayout: ConstraintLayout
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        rootLayout = findViewById(R.id.splashActivity_constrainLayout)

        FirebaseApp.initializeApp(this)

        //remote config 초기 셋팅 작업
        remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1000
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        //기본 xml폴더 밑에 만든 디폴스 컨피그 xml 설정
        //서버에서 값을 받아오지 않을 때 기본으로 나올 xml설정이라는 뜻인듯
        remoteConfig.setDefaultsAsync(R.xml.default_config)


        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(TAG, "Config params updated: $updated")
                } else {
                    Toast.makeText(
                        this, "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                displayMessage()
            }
    }

    private fun displayMessage() {
        val splash_background = remoteConfig.getString("splash_background")
        val caps = remoteConfig.getBoolean("splash_message_caps")
        val splash_message = remoteConfig.getString("splash_message")

        rootLayout.setBackgroundColor(Color.parseColor(splash_background))

        if (caps){
            val builder = AlertDialog.Builder(this)
            builder.setMessage(splash_message).setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                finish()
            })

            builder.create().show()
        }else{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}