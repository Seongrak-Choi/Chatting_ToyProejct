package com.example.chatting_toyproject.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatting_toyproject.R
import com.example.chatting_toyproject.adapter.PeopleFragmentRecyclerViewAdapter
import com.example.chatting_toyproject.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PeopleFragment : Fragment() {
    private lateinit var rcAdapter : PeopleFragmentRecyclerViewAdapter
    private lateinit var recyclerView : RecyclerView
    val userModels = ArrayList<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcAdapter = PeopleFragmentRecyclerViewAdapter(userModels,requireContext())
        recyclerView = view.findViewById(R.id.peopleFragment_recyclerView)!!
        recyclerView?.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView?.adapter = rcAdapter

        getUserList()
    }


    private fun getUserList(){
        FirebaseDatabase.getInstance().reference.child("users").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userModels.clear()
                for (i in snapshot.children){
                    userModels.add(i.getValue(UserModel::class.java)!!)
                }

                rcAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}