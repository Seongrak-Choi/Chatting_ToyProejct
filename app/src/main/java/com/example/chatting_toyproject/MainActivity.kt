package com.example.chatting_toyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatting_toyproject.fragment.ChatFragment
import com.example.chatting_toyproject.fragment.PeopleFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.mainActivity_bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.action_people ->{
                    supportFragmentManager.beginTransaction().replace(R.id.mainActivity_frameLayout,PeopleFragment()).commit()
                    true
                }
                R.id.action_chat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.mainActivity_frameLayout,ChatFragment()).commit()
                    true
                }
            }
            false
        }


    }
}