package com.example.gieok_moa

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.gieok_moa.databinding.FragmentInfiniteBinding

//main <- infiniteFragment를 설정하여 씌우는 adapter

class InfiniteFragmentPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    //override fun ViewHolder(val binding: FragmentInfiniteBinding) :

    val FragmentPosition = Int.MAX_VALUE/2

    override fun getItemCount(): Int = Int.MAX_VALUE
    override fun createFragment(position: Int): Fragment {
        val infiniteFragment = InfiniteFragment()
        infiniteFragment.pageIndex = position
        return infiniteFragment
    }
    //각 페이지마다 현재 년도, 현재 달, 다음 달, 이전 달 설정해주기
    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        super.onBindViewHolder(holder, position, payloads)
    }
}