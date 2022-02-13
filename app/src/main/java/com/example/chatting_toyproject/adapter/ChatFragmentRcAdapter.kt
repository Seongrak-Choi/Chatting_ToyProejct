package com.example.chatting_toyproject.adapter

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.chatting_toyproject.R
import com.example.chatting_toyproject.chat.MessageActivity
import com.example.chatting_toyproject.model.ChatModel
import com.example.chatting_toyproject.model.Comment
import com.example.chatting_toyproject.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatFragmentRcAdapter(
    private val chatList: ArrayList<ChatModel>,
    val context: Context,
    private val mUid: String
) : RecyclerView.Adapter<ChatFragmentRcAdapter.ViewHolder>() {

    private var destinationUsers=ArrayList<String>()
    private var simpleDateFormat = SimpleDateFormat("yyyy.MM.dd hh:mm")
    private var lastMsgKey = ""
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatFragmentRcAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatFragmentRcAdapter.ViewHolder, position: Int) {
        var destinationUid = ""
        var imgUrl : String =""
        var title : String = ""

        for (user in chatList[position].users.keys){
            if (user != mUid){
                destinationUid = user
                destinationUsers.add(user)
            }
        }

        FirebaseDatabase.getInstance().reference.child("users").child(destinationUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val current = snapshot.getValue(UserModel::class.java)

                    imgUrl = current!!.profileUrl
                    title = current!!.userName

                    val commentMap = TreeMap<String,Comment>(Collections.reverseOrder())
                    commentMap.putAll(chatList[position].comments)
                    lastMsgKey = commentMap.keys.toTypedArray()[0]
                    val lastMsg = chatList[position].comments.get(lastMsgKey)!!.message

                    simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                    val unixTime:Long = chatList[position].comments[lastMsgKey]!!.timeStamp as Long
                    val date = Date(unixTime)

                    holder.bind(imgUrl,lastMsg,title,date)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })



            holder.itemView.setOnClickListener {
            val intent = Intent(context,MessageActivity::class.java)
            intent.putExtra("destinationUid",destinationUsers[position])
            val activityOptions = ActivityOptions.makeCustomAnimation(context,R.anim.from_right,R.anim.to_left)
            context.startActivity(intent,activityOptions.toBundle())
        }
    }

    override fun getItemCount(): Int = chatList.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var iv: ImageView
        private lateinit var tvTitle: TextView
        private lateinit var tvMessage: TextView
        private lateinit var tvTimeStamp : TextView

        fun bind(imgUrl: String,lastMsg:String,title:String,date:Date) {
            iv = view.findViewById(R.id.chatItem_iv)
            tvTitle = view.findViewById(R.id.chatItem_tv_title)
            tvMessage = view.findViewById(R.id.chatItem_tv_lastMessage)
            tvTimeStamp = view.findViewById(R.id.chatItem_tv_time_stamp)

            tvTitle.text = title

            Glide.with(view.context)
                .load(imgUrl)
                .apply(RequestOptions().circleCrop())
                .into(iv)

            tvMessage.text = lastMsg
            tvTimeStamp.text=simpleDateFormat.format(date)
        }
    }

}