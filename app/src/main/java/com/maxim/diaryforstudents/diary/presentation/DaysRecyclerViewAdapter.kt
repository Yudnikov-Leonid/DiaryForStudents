package com.maxim.diaryforstudents.diary.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.databinding.FragmentDaysBinding

class DaysRecyclerViewAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<DaysRecyclerViewAdapter.ItemViewHolder>() {
    private val firstList = mutableListOf<DayUi>()
    private val secondList = mutableListOf<DayUi>()
    private val thirdList = mutableListOf<DayUi>()

    class ItemViewHolder(private val binding: FragmentDaysBinding, private val listener: Listener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(days: List<DayUi>) {
            if (days.isEmpty()) return

            days[0].showDayOfTheWeek(binding.dayTextViewOne)
            days[0].showDate(binding.dateTextViewOne)
            days[0].setSelectedColor(binding.dateTextViewOne, binding.dayTextViewOne)
            binding.dayItemViewOne.setOnClickListener {
                days[0].selectDay(listener)
            }

            days[1].showDayOfTheWeek(binding.dayTextViewTwo)
            days[1].showDate(binding.dateTextViewTwo)
            days[1].setSelectedColor(binding.dateTextViewTwo, binding.dayTextViewTwo)
            binding.dayItemViewTwo.setOnClickListener {
                days[1].selectDay(listener)
            }

            days[2].showDayOfTheWeek(binding.dayTextViewThree)
            days[2].showDate(binding.dateTextViewThree)
            days[2].setSelectedColor(binding.dateTextViewThree, binding.dayTextViewThree)
            binding.dayItemViewThree.setOnClickListener {
                days[2].selectDay(listener)
            }

            days[3].showDayOfTheWeek(binding.dayTextViewFour)
            days[3].showDate(binding.dateTextViewFour)
            days[3].setSelectedColor(binding.dateTextViewFour, binding.dayTextViewFour)
            binding.dayItemViewFour.setOnClickListener {
                days[3].selectDay(listener)
            }

            days[4].showDayOfTheWeek(binding.dayTextViewFive)
            days[4].showDate(binding.dateTextViewFive)
            days[4].setSelectedColor(binding.dateTextViewFive, binding.dayTextViewFive)
            binding.dayItemViewFive.setOnClickListener {
                days[4].selectDay(listener)
            }

            days[5].showDayOfTheWeek(binding.dayTextViewSix)
            days[5].showDate(binding.dateTextViewSix)
            days[5].setWeekendSelectedColor(binding.dateTextViewSix, binding.dayTextViewSix)
            binding.dayItemViewSix.setOnClickListener {
                days[5].selectDay(listener)
            }

            days[6].showDayOfTheWeek(binding.dayTextViewSeven)
            days[6].showDate(binding.dateTextViewSeven)
            days[6].setWeekendSelectedColor(binding.dateTextViewSeven, binding.dayTextViewSeven)
            binding.dayItemViewSeven.setOnClickListener {
                days[6].selectDay(listener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            FragmentDaysBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun getItemCount() = 3

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(
            when (position) {
                0 -> firstList
                1 -> secondList
                else -> thirdList
            }
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(daysOne: List<DayUi>, daysTwo: List<DayUi>, daysThree: List<DayUi>) {
        firstList.clear()
        secondList.clear()
        thirdList.clear()
        firstList.addAll(daysOne)
        secondList.addAll(daysTwo)
        thirdList.addAll(daysThree)
        notifyDataSetChanged()
    }

    interface Listener {
        fun selectDay(day: Int)
    }
}