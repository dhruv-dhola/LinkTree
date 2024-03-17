package com.example.project1

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.Models.Post
import com.example.project1.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var adapter: PostAdapter? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var logoutBtn: ImageView
    private lateinit var candidateBtn: ImageView
    val database = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.initView()
    }

    fun initView() {
        auth = Firebase.auth
        logoutBtn = findViewById(R.id.logoutBtn)
        candidateBtn = findViewById(R.id.candidateBtn)
        logoutBtn.setOnClickListener {
            this.logOutBtnClick()
        }

        candidateBtn.setOnClickListener {
            this.routeToCandidate()
        }


       fetchPostsAndSetupRecyclerView()
    }

    fun fetchPostsAndSetupRecyclerView() {

        //Fetch only those post which is connected to the current loggedin user
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val database = FirebaseDatabase.getInstance().reference
            //Fetch current user connections
            database.child("connections").child(currentUser.uid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get all the ids
                    val connectedUserIds = snapshot.children.mapNotNull { it.key }

                    if (!connectedUserIds.isEmpty()) {
                        //Fetch post according to above collected ids
                        val query = database.child("Posts").orderByKey().startAt(connectedUserIds.first()).endAt(connectedUserIds.last())
                        val options = FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post::class.java)
                        .build()
                    // setup recyclerview
                    setupRecyclerView(options)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseData", "Error getting data", error.toException())
                }
            })
        }
    }

    fun setupRecyclerView(options: FirebaseRecyclerOptions<Post>) {
        adapter = PostAdapter(options)

        val rView : RecyclerView = findViewById(R.id.recyclerView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter
        adapter?.startListening()
        adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        fetchPostsAndSetupRecyclerView()
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