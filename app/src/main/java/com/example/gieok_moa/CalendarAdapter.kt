package com.example.gieok_moa

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gieok_moa.databinding.ListItemCalendarBinding
import java.util.Date

class CalendarAdapter(val date:Date) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class CalendarViewHolder(val binding: ListItemCalendarBinding) : RecyclerView.ViewHolder(binding.root)
    var dataList: ArrayList<Int> = arrayListOf()

    //moacalendar을 이용해서 날짜 리스트 세팅
    var moaCalendar: MoaCalendar = MoaCalendar(date)
    init {
        moaCalendar.initBaseCalendar()
        dataList = moaCalendar.dateList
    }

    //날짜 클릭 기능은 나중에 구현하기

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO()
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}