package com.example.gieok_moa

import android.content.Context
import android.os.Binder
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.gieok_moa.databinding.FragmentInfiniteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale

class InfiniteFragment : Fragment() {
    var pageIndex:Int = 0
    companion object {
        var instance : InfiniteFragment? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
    }
    //각 페이지마다 년도, 현재 달, 이전 달, 다음 달 설정해주기
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("ju","${pageIndex}")
        val binding = FragmentInfiniteBinding.inflate(inflater,container,false)
        // 페이지 인덱스마다 보여 줄 달 설정
        val currentCalendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, pageIndex)
        }

        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH) + 1

        // 이전 월 계산
        val pastCalendar = Calendar.getInstance().apply {
            time = currentCalendar.time
            add(Calendar.MONTH, -1)
        }

        val pastMonth = pastCalendar.get(Calendar.MONTH) + 1
        // 다음 월 계산
        val nextCalendar = Calendar.getInstance().apply {
            time = currentCalendar.time
            add(Calendar.MONTH, 1)
        }
        val nextMonth = nextCalendar.get(Calendar.MONTH) + 1

        //각 값 표시
        binding.year.text = currentYear.toString()
        binding.month.text = currentMonth.toString() + "월"
        binding.pastmonth.text = pastMonth.toString() + "월"
        binding.nextmonth.text = nextMonth.toString() + "월"

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
