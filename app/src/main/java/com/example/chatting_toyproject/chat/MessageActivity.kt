package com.example.chatting_toyproject.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.chatting_toyproject.R
import com.example.chatting_toyproject.model.ChatModel
import com.example.chatting_toyproject.model.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MessageActivity : AppCompatActivity() {
    private lateinit var destinationUid : String
    private lateinit var button : Button
    private lateinit var editText: EditText

    private lateinit var uid : String
    private var chatRoomUid : String = "No"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        destinationUid = intent.getStringExtra("destinationUid").toString() //채팅을 당하는 아이디
        uid = FirebaseAuth.getInstance().currentUser!!.uid  //채팅을 요구하는 아이디 즉 단말기에 로그인된 UID

        button = findViewById(R.id.messageActivity_btn)
        editText = findViewById(R.id.messageActivity_edt)

        button.setOnClickListener {
            val chatModel = ChatModel()
            chatModel.users[uid] = true
            chatModel.users[destinationUid] = true


            if(chatRoomUid=="No"){
                FirebaseDatabase.getInstance().reference.child("chatRooms").push().setValue(chatModel)
                checkChatRoom()
            }else{
                val comment = Comment()
                comment.uid = uid
                comment.message = editText.text.toString()

                FirebaseDatabase.getInstance().reference.child("chatRooms").child(chatRoomUid).child("comments").push().setValue(comment)
            }
        }

        checkChatRoom()
    }

    fun checkChatRoom(){
        FirebaseDatabase.getInstance().reference.child("chatRooms").orderByChild("users/$uid").equalTo(true)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
                        val chatModel = item.getValue(ChatModel::class.java)
                        println("chatModel:$chatModel")
                        println("item:$item")

                        if (chatModel!!.users.containsKey(destinationUid)){
                            chatRoomUid = item.key.toString() //push로 만든 방 key값을 불러오는 코드인 듯
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
}