package com.example.project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class CandidateDetailsActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_details)

        backBtn = findViewById(R.id.backButton)
        backBtn.setOnClickListener {
            finish()
        }
    }
}