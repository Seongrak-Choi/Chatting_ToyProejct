package com.example.chatting_toyproject.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.chatting_toyproject.R
import com.example.chatting_toyproject.model.Comment
import com.example.chatting_toyproject.model.UserModel

class MessageActivityRcAdapter(private val messageList:ArrayList<Comment>,private val destinationUserInfo:UserModel,private val myUid:String) : RecyclerView.Adapter<MessageActivityRcAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageActivityRcAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageActivityRcAdapter.ViewHolder, position: Int) {
        val current = messageList[position]

        holder.bind(current)
    }

    override fun getItemCount(): Int = messageList.size

    inner class ViewHolder(private val view : View):RecyclerView.ViewHolder(view){
        private lateinit var tvMessage : TextView
        private lateinit var ivProfile : ImageView
        private lateinit var tvName : TextView
        private lateinit var linearMessage : LinearLayoutCompat
        private lateinit var linearMessageMain : LinearLayoutCompat

        fun bind(data:Comment){
            tvMessage = view.findViewById(R.id.messageItem_tv_message)
            ivProfile = view.findViewById(R.id.messageItem_iv_profile)
            tvName = view.findViewById(R.id.messageItem_tv_name)
            linearMessage = view.findViewById(R.id.messageItem_linear_layout_message)
            linearMessageMain = view.findViewById(R.id.messageItem_linearLayout_main)



            if (data.uid==myUid){
                //내가 보낸 메세지
                tvMessage.text=data.message
                linearMessage.setBackgroundResource(R.drawable.ic_out_message)
                ivProfile.visibility=View.INVISIBLE
                tvName.visibility=View.INVISIBLE
                linearMessageMain.gravity=Gravity.RIGHT
            }else{
                //상대방이 보낸 메세지
                tvMessage.text=data.message
                linearMessage.setBackgroundResource(R.drawable.ic_in_message)
                tvName.text=destinationUserInfo.userName

                ivProfile.visibility=View.VISIBLE
                tvName.visibility=View.VISIBLE

                linearMessageMain.gravity=Gravity.LEFT


                Glide.with(view.context)
                    .load(destinationUserInfo.profileUrl)
                    .apply(RequestOptions().circleCrop())
                    .into(ivProfile)
            }


        }
    }


}