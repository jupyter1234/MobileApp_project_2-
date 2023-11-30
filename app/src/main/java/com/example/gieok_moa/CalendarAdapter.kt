package com.example.gieok_moa

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gieok_moa.databinding.ListItemCalendarBinding

class CalendarAdapter(val datas : MutableList<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class CalendarViewHolder(val binding: ListItemCalendarBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}