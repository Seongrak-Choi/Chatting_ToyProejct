package com.example.chatting_toyproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatting_toyproject.R
import com.example.chatting_toyproject.adapter.ChatFragmentRcAdapter
import com.example.chatting_toyproject.model.ChatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.ktx.storageMetadata

class ChatFragment : Fragment() {

    private var chatModelsList = ArrayList<ChatModel>()
    private lateinit var rcView : RecyclerView
    private lateinit var adapter: ChatFragmentRcAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        rcView = view.findViewById(R.id.chatFragment_rc)
        adapter = ChatFragmentRcAdapter(chatModelsList,requireContext(),uid)
        rcView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        rcView.adapter = adapter

        getChatRoomList(uid)
    }


    private fun getChatRoomList(uid:String){
        FirebaseDatabase.getInstance().reference.child("chatRooms").orderByChild("users/$uid").equalTo(true).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children){
                    chatModelsList.add(item.getValue(ChatModel::class.java)!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}