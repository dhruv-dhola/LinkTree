package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.project1.Models.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson

class CandidateDetailsActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var candidateName: TextView
    private lateinit var countryTextField: TextView
    private lateinit var roleTextField: TextView
    private lateinit var experienceField: TextView
    private lateinit var skillsField: TextView
    private lateinit var certificateField: TextView
    private lateinit var aboutField: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var educationField: TextView
    private lateinit var userDetails: User
    private lateinit var connectBtn: Button
    private lateinit var database: FirebaseDatabase
    private var isConnected: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_details)
        database = Firebase.database
        backBtn = findViewById(R.id.backButton)
        backBtn.setOnClickListener {
            finish()
        }

        bindWidgets()
    }

    fun bindWidgets() {
        candidateName = findViewById(R.id.cName)
        countryTextField = findViewById(R.id.ccountry)
        roleTextField = findViewById(R.id.crole)
        experienceField = findViewById(R.id.cexperience)
        skillsField = findViewById(R.id.cDescription)
        certificateField = findViewById(R.id.idCertificates)
        aboutField = findViewById(R.id.idAbout)
        profileImageView = findViewById(R.id.profileImage)
        educationField = findViewById(R.id.idEducation)
        connectBtn = findViewById(R.id.connectBtn)
        setCandidateDetails()
    }

    fun connectBtnClick() {
        // connect and disconnect logic
        val currentUser = FirebaseAuth.getInstance().currentUser
        val candidateId = userDetails.uid
        Log.i("connectewd", isConnected.toString())
        if (isConnected) {
            Log.i("ddddd", candidateId)
            if (currentUser != null && candidateId != null) {
                database.reference.child("connections").child(currentUser.uid).child(candidateId).removeValue()
                checkUserIsConnected()
            }

        } else {
            if (currentUser != null) {
                database.reference.child("connections").child(currentUser.uid).child(userDetails.uid).setValue(true)
                checkUserIsConnected()
            }
        }
    }

    fun setCandidateDetails() {
        val i = intent
        val productStr: String = i.getStringExtra("candidate").toString()
        userDetails = Gson().fromJson(productStr, User::class.java)

        candidateName.text = userDetails.name
        countryTextField.text = userDetails.country
        roleTextField.text = userDetails.role
        experienceField.text = userDetails.experience
        skillsField.text = userDetails.skills
        certificateField.text = userDetails.certificate
        aboutField.text = userDetails.description
        educationField.text = userDetails.education
        val storageRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(userDetails.photo)
        Glide.with(this).load(storageRef).into(profileImageView)
        checkUserIsConnected()
        connectBtn.setOnClickListener {
            connectBtnClick()
        }
    }

    //Check if user connected to this candidate or not
    fun checkUserIsConnected() {

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val candidateId = userDetails.uid
        if (currentUserId != null) {
            //Fetch all the ids from the current user connections
            val connectionsRef = FirebaseDatabase.getInstance().reference.child("connections").child(currentUserId)
            connectionsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val ids = dataSnapshot.children.mapNotNull { it.key }
                    //Check if current candidate id is available in user connections
                    if (ids.contains(candidateId)) {
                        isConnected = true
                        connectBtn.text = "Connected"

                    } else {
                        isConnected = false
                        connectBtn.text = "Connect"
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FirebaseData", "Error checking connection", databaseError.toException())
                }
            })
        }


    }
}