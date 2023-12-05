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
import androidx.core.content.ContentProviderCompat
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.gieok_moa.databinding.FragmentInfiniteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.TestOnly
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class InfiniteFragment : Fragment() {
    var pageIndex = 0
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
        val currentContext: Context = this.requireContext()

        //snap 저장할 배열
        lateinit var snapDatas: MutableList<Snap>
        //디비 가져오기
        val db = UserDatabase.getInstance(currentContext.applicationContext)
        val loading = CoroutineScope(Dispatchers.IO).launch {
            snapDatas = db!!.snapDao().getAll().toMutableList()
        }

        //데이터 가져올 때까지 대기
        runBlocking {
            loading.join()
        }


        Log.d("snap","snapdata : $snapDatas")

        Log.d("ju","${pageIndex}")
        val binding = FragmentInfiniteBinding.inflate(inflater,container,false)
        // 페이지 인덱스마다 보여 줄 달 설정
        val currentCalendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, (pageIndex - Int.MAX_VALUE / 2))
        }
        //db
        fun getTagsByDate(start: Calendar,end: Calendar):List<Tag>{
            val db = UserDatabase.getInstance(requireContext().applicationContext)
            lateinit var snaps : MutableList<Snap>
            lateinit var tags: MutableList<Tag>
            val loading = CoroutineScope(Dispatchers.IO).launch {
                snaps = db!!.snapDao().getAll().toMutableList()
                tags = db.tagDao().getAll().toMutableList()
            }
            runBlocking {
                loading.join()
            }//데이터 다 가져올 때 까지 wait

            snaps.removeIf {it.createdDate.time < start.time.time || it.createdDate.time >= end.time.time }
            tags.removeIf {
                val i = it.ownedSnapID
                snaps.any { it.snapId == i }}

            return tags.toList()
        }

        fun getTagsOfMonth():List<Tag>{
            val start = currentCalendar
            start.set(Calendar.DAY_OF_MONTH, 1)
            start.set(Calendar.HOUR_OF_DAY, 0)
            start.set(Calendar.MINUTE, 0)
            start.set(Calendar.SECOND, 0)
            start.set(Calendar.MILLISECOND, 0)
            val end = (start.clone() as Calendar)
            end.add(Calendar.MONTH, 1)
            return getTagsByDate(start, end)
        }
        //이동한 페이지의 첫번째 날짜 선택 -> 이 날짜로 캘린더 만들어야됨
        // val date = currentCalendar.get(Calendar.TIME)

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

        //날짜 표시를 위한 recyclerview 등록
        binding.daysItem.adapter = CalendarAdapter(currentCalendar)
        binding.daysItem.layoutManager = GridLayoutManager(this.context,7)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
