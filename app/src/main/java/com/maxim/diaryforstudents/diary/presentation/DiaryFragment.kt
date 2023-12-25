package com.maxim.diaryforstudents.diary.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentDiaryBinding

class DiaryFragment: BaseFragment<FragmentDiaryBinding, DiaryViewModel>() {
    override val viewModelClass: Class<DiaryViewModel>
        get() = DiaryViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.back()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        val lessonsAdapter = DiaryLessonsAdapter()
        binding.lessonsRecyclerView.adapter = lessonsAdapter

        val daysAdapter = DiaryDaysAdapter()
        binding.daysRecyclerView.adapter = daysAdapter

        binding.moveLeftButton.setOnClickListener {
            viewModel.previousDay()
        }
        binding.moveRightButton.setOnClickListener {
            viewModel.nextDay()
        }

        viewModel.observe(this) {
            it.show(lessonsAdapter, daysAdapter)
        }
        viewModel.init()
    }
}