package com.example.project1

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project1.Models.Post
import com.example.project1.Models.User
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson

class CandidateAdapter(options: FirebaseRecyclerOptions<User>) : FirebaseRecyclerAdapter<User, CandidateAdapter.MyViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: User) {
        holder.txtCountry.text = model.country
        holder.txtRole.text = model.role
        holder.txtName.text = model.name
        val storageRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.photo)
        Glide.with(holder.itemView.context).load(storageRef).into(holder.userProfile)
        holder.itemView.setOnClickListener {
            val i = Intent(it.context, CandidateDetailsActivity::class.java)
            val tempObj = Gson().toJson(model)
            i.putExtra("candidate", tempObj)
            it.context.startActivity(i)
        }
    }

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.candidate_collection, parent, false))
    {
        val txtRole: TextView = itemView.findViewById(R.id.roleTxt)
        val txtName: TextView = itemView.findViewById(R.id.nameTxt)
        val txtCountry: TextView = itemView.findViewById(R.id.countryTxt)
        val userProfile: ImageView = itemView.findViewById(R.id.userProfile)


    }

}