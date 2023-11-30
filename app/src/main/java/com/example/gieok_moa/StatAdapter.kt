package com.example.gieok_moa

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gieok_moa.databinding.ItemStatBinding
import java.text.SimpleDateFormat

class StatAdapter(val datas: List<Snap>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    class MyViewHolder(val binding: ItemStatBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  =
        MyViewHolder(ItemStatBinding.inflate(LayoutInflater.from(parent.context),parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val item = datas[position]


        binding.snapTime.text = SimpleDateFormat("hh:mm").format(item.createdDate)
        Glide.with(binding.root)
            .load(item.photoUrl.toUri())
            .into(binding.snapImage)//glide라이브러리로 표시
    }
    override fun getItemCount(): Int = datas.size
}