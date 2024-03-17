package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SplashActivity : AppCompatActivity() {

    private lateinit var joinBtn: Button
    private lateinit var loginBtn: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth
        bindwidgets()
    }

    override fun onStart() {
        super.onStart()

        // Here we check if user already login or not
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    fun bindwidgets() {

        joinBtn = findViewById(R.id.join_btn)
        loginBtn = findViewById(R.id.signin_btn)

        routeLogin()
        routeSignUp()
    }

    fun routeSignUp() {
        joinBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun routeLogin() {
        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}