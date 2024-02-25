package com.maxim.diaryforstudents.diary.presentation

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.io.Serializable

class DaysAdapter(
    fragment: FragmentActivity
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = if (listener == null) 0 else 3

    private val firstList = mutableListOf<DayUi>()
    private val secondList = mutableListOf<DayUi>()
    private val thirdList = mutableListOf<DayUi>()
    private var listener: Listener? = null

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DaysFragment.newInstance(firstList, listener!!)
            1 -> DaysFragment.newInstance(secondList, listener!!)
            else -> DaysFragment.newInstance(thirdList, listener!!)
        }
    }

    override fun getItemId(position: Int): Long {
        // generate new id
        return when (position) {
            0 -> firstList.hashCode().toLong()
            1 -> secondList.hashCode().toLong()
            else -> thirdList.hashCode().toLong()
        }
    }

    override fun containsItem(itemId: Long): Boolean {
        // false if item is changed
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(firstList: List<DayUi>, secondList: List<DayUi>, thirdList: List<DayUi>, listener: Listener) {
        this.firstList.clear()
        this.secondList.clear()
        this.thirdList.clear()
        this.firstList.addAll(firstList)
        this.secondList.addAll(secondList)
        this.thirdList.addAll(thirdList)
        this.listener = listener
        notifyDataSetChanged()
    }

    interface Listener: Serializable {
        fun selectDay(day: Int)
    }
}