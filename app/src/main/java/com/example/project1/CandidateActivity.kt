package com.example.project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.Models.Post
import com.example.project1.Models.User
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class CandidateActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var backBtn: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CandidateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)

        initView()
    }

    fun initView() {
        backBtn = findViewById(R.id.backBtn)

        val query = FirebaseDatabase.getInstance().reference.child("Candidate")
        query.get().addOnCompleteListener {  }
        val options = FirebaseRecyclerOptions.Builder<User>().setQuery(query, User::class.java).build()
        adapter = CandidateAdapter(options)

        recyclerView = findViewById(R.id.collectionView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        backBtn.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

}