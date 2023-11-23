package com.example.gieok_moa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gieok_moa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    class MainFragmentPagerAdapter(activity: FragmentActivity) :
        FragmentStateAdapter(activity) {
        val fragments: List<Fragment>
        init {
            fragments = listOf(Stat2Fragment())
            //fragments = listOf(MainFragment(),Stat1Fragment(),Stat2Fragment())
        }
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = MainFragmentPagerAdapter(this)
        binding.viewPager.adapter = adapter
    }
}
