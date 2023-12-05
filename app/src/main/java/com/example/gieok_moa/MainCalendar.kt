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

        makeDummy()
    }

    fun makeDummy(){
        Log.d("ju","여기 들어옴")
        val db = UserDatabase.getInstance(this.applicationContext)
        //스냅 & 태그 추가
        CoroutineScope(Dispatchers.IO).launch {
            val imageUri = "".toUri()
            //db!!.snapDao().deleteAll()
            val snap1 = Snap(createdDate = Date(),photoUrl = imageUri.toString(),comment = "00")
            val snap2 = Snap(createdDate = Date(),photoUrl = imageUri.toString(),comment = "01")
            val snap3 = Snap(createdDate = Date(),photoUrl = imageUri.toString(),comment = "03")
            db!!.snapDao().insertAll(snap1,snap2,snap3)

            val tag1 = Tag(snap1.snapId, "good",Color.GREEN)
            val tag2 = Tag(snap2.snapId, "bad",Color.RED)
            val tag3 = Tag(snap3.snapId, "bad",Color.RED)
            db!!.tagDao().insertAll(tag1,tag2,tag3)
        }

    }
}