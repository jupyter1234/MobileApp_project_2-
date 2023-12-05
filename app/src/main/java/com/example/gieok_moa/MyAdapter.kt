package com.example.gieok_moa

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gieok_moa.databinding.ItemSnapBinding
import java.text.SimpleDateFormat

class MyAdapter(val datas: MutableList<Snap>, val clickListener: (Snap)->Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var binding: ItemSnapBinding
    inner class MyViewHolder(val binding: ItemSnapBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Snap){
            if (item.photoUrl != "android.resource://com.example.gieok_moa/drawable/snap_add_button1") {
                binding.snapTime.text = SimpleDateFormat("HH:mm").format(item.createdDate)
            }
            Glide.with(binding.root)
                .load(item.photoUrl.toUri())
                .into(binding.snapImage)
            // add tag...
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemSnapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = datas[position]
        (holder as MyViewHolder).bind(item)

        binding.root.setOnClickListener {
            clickListener(item)
        }
//        val binding=(holder as MyViewHolder).binding
//        binding.snapTime.text= datas[position].time
//        binding.snapImage.setImageResource(datas[position].image)
    }
    override fun getItemCount(): Int = datas.size
}