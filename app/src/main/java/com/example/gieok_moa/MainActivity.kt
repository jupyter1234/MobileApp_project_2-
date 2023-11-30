package com.example.gieok_moa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gieok_moa.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), OnSnapAddedListener {

    class MainFragmentPagerAdapter(activity: FragmentActivity) :
        FragmentStateAdapter(activity) {
        val fragments: List<Fragment>
        init {

            fragments = listOf(MainFragment.newInstance("",""),Stat1Fragment(),Stat2Fragment())
        }
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]

        private val fragmentHashMap = HashMap<Int, Fragment?>()


    }

    lateinit var adapter : MainFragmentPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MainFragmentPagerAdapter(this)
        binding.viewPager.adapter = adapter
    }

    override fun onSnapAdded() {
        val stat1Fragment = adapter.fragments[1] as Stat1Fragment
        stat1Fragment.updateStat1()
        val stat2Fragment = adapter.fragments[2] as Stat2Fragment
        stat2Fragment.refresh()

    }
}
