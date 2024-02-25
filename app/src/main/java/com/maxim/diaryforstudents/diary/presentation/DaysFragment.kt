package com.maxim.diaryforstudents.diary.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.databinding.FragmentDaysBinding

class DaysFragment: BaseFragment<FragmentDaysBinding, DaysViewModel>() {
    override val viewModelClass = DaysViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDaysBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val days = mutableListOf<DayUi>()
        (0..6).forEachIndexed { i, _ ->
            days.add(requireArguments().getSerializable("$KEY$i") as DayUi)
        }

        val onClick = requireArguments().getSerializable(LAMBDA_KEY) as DaysAdapter.Listener

        days[0].showDayOfTheWeek(binding.dayTextViewOne)
        days[0].showDate(binding.dateTextViewOne)
        days[0].setSelectedColor(binding.dateTextViewOne, binding.dayTextViewOne)
        binding.dayItemViewOne.setOnClickListener {
            days[0].selectDay(onClick)
        }

        days[1].showDayOfTheWeek(binding.dayTextViewTwo)
        days[1].showDate(binding.dateTextViewTwo)
        days[1].setSelectedColor(binding.dateTextViewTwo, binding.dayTextViewTwo)
        binding.dayItemViewTwo.setOnClickListener {
            days[1].selectDay(onClick)
        }

        days[2].showDayOfTheWeek(binding.dayTextViewThree)
        days[2].showDate(binding.dateTextViewThree)
        days[2].setSelectedColor(binding.dateTextViewThree, binding.dayTextViewThree)
        binding.dayItemViewThree.setOnClickListener {
            days[2].selectDay(onClick)
        }

        days[3].showDayOfTheWeek(binding.dayTextViewFour)
        days[3].showDate(binding.dateTextViewFour)
        days[3].setSelectedColor(binding.dateTextViewFour, binding.dayTextViewFour)
        binding.dayItemViewFour.setOnClickListener {
            days[3].selectDay(onClick)
        }

        days[4].showDayOfTheWeek(binding.dayTextViewFive)
        days[4].showDate(binding.dateTextViewFive)
        days[4].setSelectedColor(binding.dateTextViewFive, binding.dayTextViewFive)
        binding.dayItemViewFive.setOnClickListener {
            days[4].selectDay(onClick)
        }

        days[5].showDayOfTheWeek(binding.dayTextViewSix)
        days[5].showDate(binding.dateTextViewSix)
        days[5].setSelectedColor(binding.dateTextViewSix, binding.dayTextViewSix)
        binding.dayItemViewSix.setOnClickListener {
            days[5].selectDay(onClick)
        }

        days[6].showDayOfTheWeek(binding.dayTextViewSeven)
        days[6].showDate(binding.dateTextViewSeven)
        days[6].setSelectedColor(binding.dateTextViewSeven, binding.dayTextViewSeven)
        binding.dayItemViewSeven.setOnClickListener {
            days[6].selectDay(onClick)
        }
    }

    companion object {
        fun newInstance(days: List<DayUi>, onClick: DaysAdapter.Listener): DaysFragment {
            return DaysFragment().apply {
                arguments = Bundle().apply {
                    days.forEachIndexed { i, dayUi ->
                        putSerializable("$KEY$i", dayUi)
                    }
                    putSerializable(LAMBDA_KEY, onClick)
                }
            }
        }

        private const val KEY = "days_key"
        private const val LAMBDA_KEY = "days_key"
    }
}

class DaysViewModel: ViewModel()
class DaysModule: Module<DaysViewModel> {
    override fun viewModel() = DaysViewModel()
}