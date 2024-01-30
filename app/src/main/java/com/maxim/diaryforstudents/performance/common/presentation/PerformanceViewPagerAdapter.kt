package com.maxim.diaryforstudents.performance.common.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.maxim.diaryforstudents.performance.actualMarks.presentation.PerformanceActualFragment
import com.maxim.diaryforstudents.performance.analytics.presentation.PerformanceAnalyticsFragment
import com.maxim.diaryforstudents.performance.finalMarks.presentation.PerformanceFinalFragment

class PerformanceViewPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PerformanceActualFragment()
            1 -> PerformanceFinalFragment()
            else -> PerformanceAnalyticsFragment()
        }
    }
}