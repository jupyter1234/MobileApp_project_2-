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

        Log.d("ju","${pageIndex}")
        val binding = FragmentInfiniteBinding.inflate(inflater,container,false)
        // 페이지 인덱스마다 보여 줄 달 설정
        val currentCalendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, (pageIndex - Int.MAX_VALUE / 2))
        }

        //해당 달에 해당하는 태그의 각 날짜별로 색깔 카운트하기 끝끝 -> adapter에 넘겨주기
        val colorList:MutableList<Int> = getTagsOfMonth(db,currentCalendar)
        Log.d("tag","$colorList")
        //해당 달에 해당하는 태그 가져오기
//        val tagsByDate:List<Tag> = getTagsOfMonth(currentCalendar)
//        Log.d("tags","$tagsByDate")
        //[Tag(tagID=4, staus=good, color=GREEN, ownedSnapID=0),...]

        //이동한 페이지의 첫번째 날짜 선택 -> 이 날짜로 캘린더 만들어야됨
        // val date = currentCalendar.get(Calendar.TIME)

        val currentYear = currentCalendar.get(Calendar.YEAR)
        //1 더해야 올바른 값임
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
        binding.month.text = currentMonth.toString() + getString(R.string.month)
        binding.pastmonth.text = pastMonth.toString() + getString(R.string.month)
        binding.nextmonth.text = nextMonth.toString() + getString(R.string.month)

        //날짜 표시를 위한 recyclerview 등록
        binding.daysItem.adapter = CalendarAdapter(currentCalendar,colorList)
        binding.daysItem.layoutManager = GridLayoutManager(this.context,7)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    //해달 달의 태그 가져오기
    fun getTagsByDate(db: UserDatabase?,start: Calendar,end: Calendar):List<Tag>{
        lateinit var snaps : MutableList<Snap>
        lateinit var tags: MutableList<Tag>
        val loading = CoroutineScope(Dispatchers.IO).launch {
            snaps = db!!.snapDao().getAll().toMutableList()
            tags = db!!.tagDao().getAll().toMutableList()
        }

        //데이터 다 가져올 때 까지 wait
        runBlocking {
            loading.join()
        }
        Log.d("snap","$snaps")
        snaps.removeIf {it.createdDate.time < start.time.time || it.createdDate.time >= end.time.time }
        tags.removeIf { tag ->
            // tag가 소유한 snapId가 snaps 목록에 존재하지 않으면 삭제
            !snaps.any { it.snapId == tag.ownedSnapID }
        }
        return tags.toList()
    }

    //해당 달의 하루에 사용한 최대 사용 색깔 리스트 반환
    fun getTagsOfMonth(db: UserDatabase?,currentCalendar:Calendar):MutableList<Int>{
        //스크롤반영한 현재 달력
        val start = currentCalendar
        start.set(Calendar.DAY_OF_MONTH,1)
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.SECOND, 0)
        //start.set(Calendar.MONTH, start.get(Calendar.MONTH)+1)
        start.set(Calendar.MILLISECOND, 0)
        val end = (start.clone() as Calendar)
        end.add(Calendar.MONTH, 1)
        val tagsbyDate: List<Tag> = getTagsByDate(db,start,end)
        //

        //해당 달의 날짜 수
        val maxDate : Int = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //하루에 해당하는 태그 저장하기
        var tagsbyDay: List<Tag> = listOf()
        //가장 많이 사용한 색깔 저장할 리스트  [빨, 노, 초]
        var topColorByDate: MutableList<Int> = mutableListOf()

        var colorMax = 3 //색깔이 없으면 3으로 저장

        start.set(Calendar.DAY_OF_MONTH,1)
        val end2 = (start.clone() as Calendar)
        //하루 단위로 태그 구하고 가장많이 사용한 태그 색깔 저장하기
        for (i in 1 .. maxDate) {
            //end2 : start 다음날
            start.set(Calendar.DAY_OF_MONTH, i)
            end2.set(Calendar.DAY_OF_MONTH, i+1)
            tagsbyDay = getTagsByDate(db,start,end2)
            colorMax = getScoreList(db,tagsbyDay)
            topColorByDate.add(colorMax)
        }
        return topColorByDate;
    }

    //태그 컬러별로 카운트해서 가져오기
    fun getScoreList(db: UserDatabase?, tag2List: List<Tag>):Int {
//        val currentMonth = currentCalendar.get(Calendar.MONTH) + 1
//        Log.d("tags","$currentMonth 에 들어옴")
        val colorList = mutableListOf<Int>(0,0,0)
        for(i in tag2List){
            when(i.color){
                Color.RED->colorList[0]+=1
                Color.YELLOW->colorList[1]+=1
                Color.GREEN->colorList[2]+=1
            }
        }
        var index: Int = colorList.indexOf(colorList.max())
        if (colorList.max() == 0){
            index = 3
        }
        return index
    }
}
