package com.example.project1

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.Models.Post
import com.example.project1.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var adapter: PostAdapter? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var logoutBtn: ImageView
    private lateinit var candidateBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.initView()
    }

    fun initView() {
        auth = Firebase.auth
        logoutBtn = findViewById(R.id.logoutBtn)
        candidateBtn = findViewById(R.id.candidateBtn)
        val query = FirebaseDatabase.getInstance().reference.child("Posts")
        val options = FirebaseRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()
        adapter = PostAdapter(options)

        val rView : RecyclerView = findViewById(R.id.recyclerView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter

        logoutBtn.setOnClickListener {
            this.logOutBtnClick()
        }

        candidateBtn.setOnClickListener {
            this.routeToCandidate()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    fun routeToCandidate() {
        val intent = Intent(this, CandidateActivity::class.java)
        startActivity(intent)
    }


    fun logOutBtnClick() {
        if (auth.getCurrentUser() != null) {
            auth.signOut()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }


}