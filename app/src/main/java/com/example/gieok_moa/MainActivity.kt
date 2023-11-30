package com.example.gieok_moa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.gieok_moa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //toolbar 뒤로가기 버튼
        var back = ContextCompat.getDrawable(this, R.drawable.arrow)
        setSupportActionBar(binding.maintoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(back)
        title = ""

        //어댑터 적용
        binding.viewpager.adapter = InfiniteFragmentPagerAdapter(this)
        binding.viewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewpager.setCurrentItem((Int.MAX_VALUE /2), false)
    }
}