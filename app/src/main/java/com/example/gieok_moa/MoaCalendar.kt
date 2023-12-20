package com.example.gieok_moa

import androidx.core.content.ContentProviderCompat.requireContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MoaCalendar(currentCalendar: Calendar) {
    //snap 저장할 배열
    lateinit var datas: MutableList<Snap>
    companion object {
        const val DAYS_OF_WEEK = 7
        const val LOW_OF_CALENDAR = 5
    }

    var calendar = currentCalendar

    //이전 달 꼬리
    var prevTail = 0
    //다음달 헤드
    //이전달 다음달 날들을 다 연결해야 해당 달 시작 요일이 정확하게 반영됨
    var nextHead = 0
    var currentMaxDate = 0

    //해당 달 날짜 기록할 배열
    var dateList = arrayListOf<Int>()

    //해당 달 날짜 태그 색깔 기록할 배열
    var top_colors = arrayListOf<Int>()

//    init {
//        calendar.time = date
//    }

    fun initBaseCalendar() {
        makeMonthDate()
    }

    private fun makeMonthDate() {

        dateList.clear()

        calendar.set(Calendar.DATE, 1)

        currentMaxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        prevTail = calendar.get(Calendar.DAY_OF_WEEK) - 1

        makePrevTail(calendar.clone() as Calendar)
        makeCurrentMonth(calendar)

        nextHead = LOW_OF_CALENDAR * DAYS_OF_WEEK - (prevTail + currentMaxDate)
        makeNextHead()
    }

    private fun makePrevTail(calendar: Calendar) {
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1)
        val maxDate = calendar.getActualMaximum(Calendar.DATE)
        var maxOffsetDate = maxDate - prevTail

        for (i in 1..prevTail) dateList.add(++maxOffsetDate)
    }
    private fun makeCurrentMonth(calendar: Calendar) {
        for (i in 1..calendar.getActualMaximum(Calendar.DATE)) dateList.add(i)
    }

    private fun makeNextHead() {
        var date = 1

        for (i in 1..nextHead) dateList.add(date++)
    }
}