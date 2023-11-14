package com.example.gieok_moa

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gieok_moa.databinding.FragmentCalendarBinding
import com.example.gieok_moa.databinding.FragmentInfiniteBinding
import java.util.Date

class CalendarFragment : Fragment() {

    var pageIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return FragmentCalendarBinding.inflate(inflater,container,false).root
    }
}