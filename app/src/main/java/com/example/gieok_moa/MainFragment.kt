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
        val snapEx = Snap("23:22", R.drawable.snap_image1, 1)
        datas.add(snapEx)
        datas.add(snapAddButton)

//        datas.add(snapEx)
//        datas.add(snapEx)
//        datas.add(snapEx)
//        datas.add(snapEx)

        val layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = MyAdapter(datas){ snap ->
            //Log.d("check", "snap clicked")
            if (snap.image == R.drawable.snap_add_button){
                // when snap add button clicked -> popup & move to snap add page

            }
            else{
                // when snap clicked -> dialog
                
            }
        }
        binding.recyclerView.adapter = adapter

        return binding.root
    }

}