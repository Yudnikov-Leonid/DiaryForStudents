package com.maxim.diaryforstudents.performance.analytics.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentFinalPerformanceBinding

class PerformanceAnalyticsFragment: BaseFragment<FragmentFinalPerformanceBinding, PerformanceAnalyticsViewModel>() {
    override val viewModelClass = PerformanceAnalyticsViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFinalPerformanceBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AnalyticsAdapter()
        binding.lessonsRecyclerView.adapter = adapter

        viewModel.observe(this) {
            it.show(adapter, binding.progressBar)
        }

        viewModel.init(savedInstanceState == null)
    }
}