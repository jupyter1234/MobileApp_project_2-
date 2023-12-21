package com.example.gieok_moa

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gieok_moa.databinding.ItemSnapBinding
import com.example.gieok_moa.databinding.ItemSpinnerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MyAdapter(val datas: MutableList<Snap>, val clickListener: (Snap)->Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var binding: ItemSnapBinding
    inner class MyViewHolder(val binding: ItemSnapBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Snap){
            if (item.photoUrl != "android.resource://com.example.gieok_moa/drawable/snap_add_button") {
                binding.snapTime.text = SimpleDateFormat("HH:mm").format(item.createdDate)
            }
            Glide.with(binding.root)
                .load(item.photoUrl.toUri())
                .into(binding.snapImage)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemSnapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = datas[position]
        (holder as MyViewHolder).bind(item)

        if (item.photoUrl != "android.resource://com.example.gieok_moa/drawable/snap_add_button"){
            Log.d("ko", "니뭔데")
            val binding = (holder as MyViewHolder).binding
            val db = UserDatabase.getInstance(holder.itemView.context)
            CoroutineScope(Dispatchers.IO).launch {
                val tags = db!!.tagDao().getAll()
                for (tag in tags){
                    if (tag.ownedSnapID == item.snapId){
                        binding.tag.text = tag.staus
                        binding.tag.background = when (tag.color) {
                            Color.RED -> ContextCompat.getDrawable(binding.root.context, R.drawable.red_blur_background)
                            Color.GREEN -> ContextCompat.getDrawable(binding.root.context, R.drawable.green_blur_background)
                            Color.YELLOW -> ContextCompat.getDrawable(binding.root.context, R.drawable.yellow_blur_background)
                        }
                    }
                }
            }
        }

        binding.root.setOnClickListener {
            clickListener(item)
        }
//        val binding=(holder as MyViewHolder).binding
//        binding.snapTime.text= datas[position].time
//        binding.snapImage.setImageResource(datas[position].image)
    }
    override fun getItemCount(): Int = datas.size
}