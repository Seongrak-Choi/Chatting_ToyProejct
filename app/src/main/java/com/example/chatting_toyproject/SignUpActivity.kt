package com.example.chatting_toyproject

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.chatting_toyproject.model.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.properties.Delegates

class SignUpActivity : AppCompatActivity() {

    private val PICK_FROM_ALBUM = 10

    private lateinit var id: EditText
    private lateinit var name: EditText
    private lateinit var password: EditText
    private lateinit var signUp: Button
    private lateinit var ivProfile: ImageView
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var imageUri: Uri
    private var isImageUriNotNull = false

    private val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //remote config에서 설정한 스플래시 백그라운드 색상을 statusbar 색상으로 적용해 줌
        remoteConfig = Firebase.remoteConfig
        val splashBackground = remoteConfig.getString("splash_background")
        window.statusBarColor = Color.parseColor(splashBackground)

        ivProfile = findViewById(R.id.signUpActivity_iv_profile)
        ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, PICK_FROM_ALBUM)
        }

        id = findViewById(R.id.signUpActivity_edt_id)
        name = findViewById(R.id.signUpActivity_edt_name)
        password = findViewById(R.id.signUpActivity_edt_pwd)
        signUp = findViewById(R.id.signUpActivity_btn_signUp)
        signUp.setBackgroundColor(Color.parseColor(splashBackground))

        signUp.setOnClickListener {
            if (!id.text.isNullOrBlank() && !name.text.isNullOrBlank() && !password.text.isNullOrBlank() && isImageUriNotNull) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(id.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { it ->
                        val uid = it.result.user?.uid

                        storageRef.child("userImages")
                            .child(uid.toString())
                            .putFile(imageUri)
                            .addOnCompleteListener {task->
                                task.result.metadata!!.reference!!.downloadUrl.addOnCompleteListener {
                                    val imageUrl = it.result.toString()

                                    val uid = FirebaseAuth.getInstance().currentUser!!.uid
                                    val userModel = UserModel(userName = name.text.toString(),profileUrl = imageUrl,uid = uid) //유저 데이터 클래스에 이름과, 프로필 사진 url을 인자로 넘겨 인스턴스 생성

                                    FirebaseDatabase.getInstance().reference.child("users")
                                        .child(uid.toString()).setValue(userModel).addOnSuccessListener {
                                            this.finish()
                                        } //위에서 만든 userModel 인스턴스를 데이터베이스에 저장

                                }
                            }
                    }
            } else {
                Toast.makeText(this, "비어있으면 안되요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            ivProfile.setImageURI(data?.data) //선택된 이미지 보일 수 있도록 이미지뷰 변경
            imageUri = data?.data!! //이미지 경로 원본 저장
            isImageUriNotNull = true
        }
    }
}