package com.example.gieok_moa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import com.example.gieok_moa.databinding.ActivityMainCalendarBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainCalendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainCalendarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //toolbar 뒤로가기 버튼
        var back = ContextCompat.getDrawable(this, R.drawable.arrow)
        setSupportActionBar(binding.maintoolbar)
        //홈으로 이동
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(back)
        title = ""

        //어댑터 적용
        binding.viewpager.adapter = InfiniteFragmentPagerAdapter(this)
        binding.viewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
        //위 아래로 무한대 스크롤 가능
        binding.viewpager.setCurrentItem((Int.MAX_VALUE /2), false)

        //makeDummy()
    }

    fun makeDummy(){
        Log.d("ju","여기 들어옴")
        val db = UserDatabase.getInstance(this.applicationContext)
        //스냅 & 태그 추가

        //11월 더미 만들기
        val date = Calendar.getInstance().run {
            add(Calendar.MONTH,-1)
            time
        }
        CoroutineScope(Dispatchers.IO).launch {
            val imageUri = "".toUri()
            //db!!.snapDao().deleteAll()
            val snap1 = Snap(createdDate = date,photoUrl = imageUri.toString(),comment = "111")
            val snap2 = Snap(createdDate = date,photoUrl = imageUri.toString(),comment = "2222")
            val snap3 = Snap(createdDate = date,photoUrl = imageUri.toString(),comment = "333")
            db!!.snapDao().insertAll(snap1,snap2,snap3)
            val tag1 = Tag(staus = "soso", color = Color.YELLOW, ownedSnapID = snap1.snapId)
            val tag2 = Tag(staus = "soso",color = Color.YELLOW, ownedSnapID = snap2.snapId)
            val tag3 = Tag(staus = "soso",color = Color.YELLOW, ownedSnapID = snap3.snapId)
            db!!.tagDao().insertAll(tag1,tag2,tag3)
        }

    }
}