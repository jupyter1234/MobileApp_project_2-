package com.example.gieok_moa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gieok_moa.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    class MainFragmentPagerAdapter(activity: FragmentActivity) :
        FragmentStateAdapter(activity) {
        val fragments: List<Fragment>
        init {

            fragments = listOf(MainFragment.newInstance("",""),Stat1Fragment(),Stat2Fragment())
        }
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]

    }

    lateinit var adapter : MainFragmentPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MainFragmentPagerAdapter(this)
        binding.viewPager.adapter = adapter

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        val usePassword = sharedPreferences.getBoolean("use_password", false)
        if(usePassword) {
            val signinIntent: Intent = Intent(this, SigninActivity::class.java)
            startActivity(signinIntent)
        }
    }
}
