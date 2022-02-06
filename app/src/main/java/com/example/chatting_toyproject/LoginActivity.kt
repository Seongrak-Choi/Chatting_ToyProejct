package com.example.chatting_toyproject

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {

    private lateinit var id : EditText
    private lateinit var pwd : EditText

    private lateinit var signIn : Button
    private lateinit var signUp : Button
    private lateinit var remoteConfig: FirebaseRemoteConfig

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //remote config에서 설정한 스플래시 백그라운드 색상을 statusbar 색상으로 적용해 줌
        remoteConfig = Firebase.remoteConfig
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        val splashBackground = remoteConfig.getString("splash_background")
        window.statusBarColor= Color.parseColor(splashBackground)

        id = findViewById(R.id.loginActivity_edt_id)
        pwd = findViewById(R.id.loginActivity_edt_pwd)
        signIn = findViewById(R.id.loginActivity_btn_singIn)
        signIn.setOnClickListener {
            loginEvent()
        }

        //로그인 인터페이스 리스너
        authStateListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null){
                //로그인
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                //로그아웃
            }
        }

        signUp = findViewById(R.id.loginActivity_btn_singUp)

        signIn.setBackgroundColor(Color.parseColor(splashBackground))
        signUp.setBackgroundColor(Color.parseColor(splashBackground))

        signUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }

    fun loginEvent(){
        firebaseAuth.signInWithEmailAndPassword(id.text.toString(),pwd.text.toString()).addOnCompleteListener {
            if (!it.isSuccessful){ //로그인 실패했을 경우
                Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
            }else{
                //로그인 성공했을 경우

            }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}