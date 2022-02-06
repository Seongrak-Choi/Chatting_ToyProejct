package com.example.chatting_toyproject.adapter

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.example.chatting_toyproject.model.UserModel

class PeopleFragmentRecyclerViewAdapter(val userModelList:ArrayList<UserModel>,val context : Context) :
    RecyclerView.Adapter<PeopleFragmentRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleFragmentRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeopleFragmentRecyclerViewAdapter.ViewHolder, position: Int) {
        val current = userModelList[position]
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,MessageActivity::class.java)
            intent.putExtra("destinationUid",userModelList[position].uid)
            val activityOptions = ActivityOptions.makeCustomAnimation(context,R.anim.from_right,R.anim.to_left) //화면 바뀔 때 애니메이션 적용
            context.startActivity(intent,activityOptions.toBundle())
        }
        holder.bind(current)
    }

    override fun getItemCount() = userModelList.size

    inner class ViewHolder(private val view:View):RecyclerView.ViewHolder(view){
        lateinit var imageView : ImageView
        lateinit var textView : TextView

        fun bind(data:UserModel){
            imageView = view.findViewById(R.id.friendItem_iv)
            textView = view.findViewById(R.id.friendItem_tv)

            Glide.with(context)
                .load(data.profileUrl)
                .apply(RequestOptions().circleCrop())
                .into(imageView)
            textView.text = data.userName
        }
    }
}