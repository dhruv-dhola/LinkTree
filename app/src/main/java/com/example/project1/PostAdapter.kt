package com.example.project1

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project1.Models.Post
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class PostAdapter(options: FirebaseRecyclerOptions<Post>) : FirebaseRecyclerAdapter<Post, PostAdapter.MyViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Post) {

        holder.txtTitle.text = model.title
        holder.txtDescription.text = model.description

        val profileUrl = model.image

        Glide.with(holder.itemView.context).load(profileUrl).into(holder.userProfile)
        Glide.with(holder.itemView.context).load(profileUrl).into(holder.image)

    }
    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.post, parent, false))
    {
        val txtTitle: TextView = itemView.findViewById(R.id.usernameText)
        val txtDescription: TextView = itemView.findViewById(R.id.descriptionText)
        val image: ImageView = itemView.findViewById(R.id.postImage)
        val userProfile: ImageView = itemView.findViewById(R.id.profileImage)

    }

}