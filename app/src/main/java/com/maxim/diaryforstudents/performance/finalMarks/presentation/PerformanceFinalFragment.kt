package com.maxim.diaryforstudents.performance.finalMarks.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentFinalPerformanceBinding
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceLessonsAdapter
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

class PerformanceFinalFragment: BaseFragment<FragmentFinalPerformanceBinding, PerformanceFinalViewModel>() {
    override val viewModelClass = PerformanceFinalViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFinalPerformanceBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PerformanceLessonsAdapter(object : PerformanceLessonsAdapter.Listener {
            override fun calculate(marks: List<PerformanceUi.Mark>, marksSum: Int) = Unit
        })
        binding.lessonsRecyclerView.adapter = adapter

        viewModel.observe(this) {
            it.show(
                adapter,
                binding.errorTextView,
                binding.retryButton,
                binding.progressBar
            )
        }

        viewModel.reload()
    }
}