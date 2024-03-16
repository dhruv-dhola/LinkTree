package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.project1.Models.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailTextField: TextInputLayout
    private lateinit var passwordTextField: TextInputLayout
    private lateinit var confirmPasswordTxtField: TextInputLayout
    private lateinit var signInBtn: TextView
    private lateinit var signUpBtn: Button
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        bindWidgets()

    }


    fun bindWidgets() {
        emailTextField = findViewById(R.id.email)
        passwordTextField = findViewById(R.id.password)
        confirmPasswordTxtField = findViewById(R.id.confirmPassword)
        signInBtn = findViewById(R.id.signInBtn)
        signUpBtn = findViewById(R.id.signupBtn)
        routeSignInScreen()

        signUpBtn.setOnClickListener {
            signUpWithFirebase()
        }
    }

    fun routeSignInScreen() {
        signInBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun routeHomeScreen() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
    }

    //Save user data after login into firebase realtime database
    fun saveUserData() {
        val user = auth.currentUser
        val userId = user?.uid ?: ""
        val email = user?.email ?: ""
        val databaseReference = database.getReference("Users").child(userId)
        val userData = User(id = userId, email = email)
        databaseReference.setValue(userData)
        this.routeHomeScreen()
    }


    // Email address validation function Which return boolean value
    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    // Firebase Sign In Function
    fun signUpWithFirebase() {
        val emailValue = emailTextField.editText?.text.toString().trim()
        val passwordValue = passwordTextField.editText?.text.toString().trim()
        val confirmPasswordValue = confirmPasswordTxtField.editText?.text.toString().trim()

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
        } else if (confirmPasswordValue.isEmpty()) {
            Toast.makeText(
                baseContext,
                "Please enter confirm password.",
                Toast.LENGTH_SHORT,
            ).show()
        } else if (confirmPasswordValue != passwordValue) {
            Toast.makeText(
                baseContext,
                "Opps, passwords doesn't match",
                Toast.LENGTH_SHORT,
            ).show()
        }
        else {
            //Email Address Validation
            if (isValidEmail(emailValue)) {
                auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign up success, go to home screen
                            this.saveUserData()
                        } else {
                            // If sign up fails, display toast message
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