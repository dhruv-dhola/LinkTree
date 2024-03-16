package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailTextField: TextInputLayout
    private lateinit var passwordTextField: TextInputLayout
    private lateinit var signInBtn: Button
    private lateinit var signUpBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        bindWidgets()

    }

    override fun onStart() {
        super.onStart()

        //Here we check if user is logged in or not
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    fun bindWidgets() {
        emailTextField = findViewById(R.id.email)
        passwordTextField = findViewById(R.id.password)
        signInBtn = findViewById(R.id.loginBtn)
        signUpBtn = findViewById(R.id.signupBtn)

        routeSignUpScreen()
        signInBtn.setOnClickListener {
            signInWithFirebase()
        }
    }

    fun routeSignUpScreen() {
        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun routeHomeScreen() {
        signInBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Email address validation function Which return boolean value
    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    // Firebase Sign In Function
    fun signInWithFirebase() {
        val emailValue = emailTextField.editText?.text.toString().trim()
        val passwordValue = passwordTextField.editText?.text.toString().trim()
        if (emailValue.isEmpty()) {
            Toast.makeText(
                baseContext,
                "Please enter email address.",
                Toast.LENGTH_SHORT,
            ).show()
        } else if (passwordValue.isEmpty()) {
            Toast.makeText(
                baseContext,
                "Please enter password.",
                Toast.LENGTH_SHORT,
            ).show()
        } else {
            //Email Address Validation
            if (isValidEmail(emailValue)) {
                auth.signInWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, go to home screen
                            routeHomeScreen()
                        } else {
                            // If sign in fails, display toast message
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    baseContext,
                    "Please enter valid email address.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}