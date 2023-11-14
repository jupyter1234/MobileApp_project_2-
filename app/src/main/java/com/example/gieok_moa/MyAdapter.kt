package com.example.gieok_moa

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gieok_moa.databinding.ItemSnapBinding

class MyAdapter(val datas: MutableList<Snap>, val clickListener: (Snap)->Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var binding: ItemSnapBinding
    inner class MyViewHolder(val binding: ItemSnapBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Snap){
            binding.snapTime.text = item.time
            binding.snapImage.setImageResource(item.image)
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