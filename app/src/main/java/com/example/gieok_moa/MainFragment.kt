package com.example.gieok_moa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gieok_moa.databinding.FragmentMainBinding
import com.example.gieok_moa.databinding.ItemSnapBinding
import java.util.Date
import java.text.SimpleDateFormat

class MyViewHolder(val binding: ItemSnapBinding) : RecyclerView.ViewHolder(binding.root)
class MyAdapter(val datas: MutableList<Snap>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemSnapBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding
        binding.snapTime.text= datas[position].time
        binding.snapImage.setImageResource(datas[position].image)
    }

    override fun getItemCount(): Int = datas.size
}
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        // loading current date
        val currentDate = SimpleDateFormat("MM/dd").format(Date())
        binding.date.text = currentDate

        binding.settingButton.setOnClickListener {
            Log.d("check", "setting button clicked")
        }

        binding.calendarButton.setOnClickListener {
            Log.d("check", "calendar button clicked")
        }

        val datas: MutableList<Snap> = mutableListOf()
        val snapAddButton: Snap = Snap("", R.drawable.snap_add_button, 0)
        val snapEx = Snap("23:22", R.drawable.snap_image, 1)
        datas.add(snapAddButton)
//        datas.add(snapEx)
//        datas.add(snapEx)
//        datas.add(snapEx)
//        datas.add(snapEx)
//        datas.add(snapEx)


        val layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = MyAdapter(datas)
        binding.recyclerView.adapter = adapter

        // when snap add button clicked


        // when snap clicked

        return binding.root
    }

}