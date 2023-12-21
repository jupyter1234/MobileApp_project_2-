package com.example.gieok_moa

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.gieok_moa.databinding.ListItemCalendarBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Date

class CalendarAdapter(val calendar: Calendar, val colorList: List<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class CalendarViewHolder(val binding: ListItemCalendarBinding) : RecyclerView.ViewHolder(binding.root)
    var dataList: ArrayList<Int> = arrayListOf()
    //moacalendar을 이용해서 날짜 리스트 세팅
    var moaCalendar: MoaCalendar = MoaCalendar(calendar)
    init {
        moaCalendar.initBaseCalendar()
        dataList = moaCalendar.dateList
    }


    //날짜 클릭 기능은 나중에 구현하기

    //항목 구성에 필요한 뷰 홀더 객체 준비
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CalendarViewHolder(ListItemCalendarBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    //현재 달 총 날짜 수
    override fun getItemCount(): Int = dataList.size

    //뷰에 데이터 출력
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as CalendarViewHolder).binding

        val context = binding.root.context
        //첫날짜
        val firstDateIndex = moaCalendar.prevTail
        //마지막 날짜
        val lastDateIndex = dataList.size - moaCalendar.nextHead - 1
        //Log.d("ju","$firstDateIndex, $lastDateIndex")
        //뷰에 데이터 출력 -> 닐짜출력, 감정 상태 설정


        Log.d("ju","datalist : ${dataList[position]}")
        binding.calendarDate.text = dataList[position].toString()
        //디비 연동 후 하루 중 가장 많이 쓴 태그 색깔 가져와서 해당하는 색깔 resource 적용하기
        // -> colorList [빨, 노, 초]
        if (position in firstDateIndex..lastDateIndex){
            when(colorList[position-firstDateIndex]){
                0 -> binding.colors.setImageResource(R.drawable.red)
                1 -> binding.colors.setImageResource(R.drawable.yellow)
                2 -> binding.colors.setImageResource(R.drawable.green)
                3 -> binding.colors.setImageResource(R.drawable.gray)
            }
        }


        //현재 달에 속하지 않는 날짜 (이전달의 tail, 다음달의 head)는 회색 처리
        if(position < firstDateIndex || position > lastDateIndex) {
            binding.calendarDate.isVisible = false
            binding.colors.isVisible = false
        }

        //날짜 클릭하면 스냅 페이지로 이동기능
        //날짜 넘겨주기 위한 데이터 세팅
        val thisDate = Calendar.getInstance()
        val currentMonth = moaCalendar.calendar.get(Calendar.MONTH)
        val currentYear = moaCalendar.calendar.get(Calendar.YEAR)
        thisDate.set(Calendar.YEAR, currentYear)
        thisDate.set(Calendar.MONTH, currentMonth)
        thisDate.set(Calendar.DATE,dataList[position] )
        binding.listItemCalendar.setOnClickListener {
            //숨김해둔 날짜라면 터치 비활성화
            if (position in firstDateIndex..lastDateIndex) {
                if (colorList[position - firstDateIndex] != 3 ) {
                    val intent = Intent(context,MainFragment::class.java)
                    val test = thisDate.time
                    Log.d("ju","$test")
                    //intent.putExtra("clickedDate",thisDate.time)
                }

            }

        }
    }
}