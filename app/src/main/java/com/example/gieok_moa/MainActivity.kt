package com.example.gieok_moa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.gieok_moa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewpager.adapter = InfiniteFragmentPagerAdapter(this)
        binding.viewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }
}