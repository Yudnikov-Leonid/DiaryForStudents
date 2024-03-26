package com.maxim.diaryforstudents.performance.finalMarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentFinalPerformanceBinding

class PerformanceFinalFragment: BaseFragment<FragmentFinalPerformanceBinding, PerformanceFinalViewModel>() {
    override val viewModelClass = PerformanceFinalViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFinalPerformanceBinding.inflate(inflater, container, false)

    override var setOnBackPressedCallback = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PerformanceFinalLessonsAdapter()
        binding.lessonsRecyclerView.adapter = adapter

        binding.retryButton.setOnClickListener {
            viewModel.retry()
        }

        viewModel.observe(this) {
            it.show(
                binding.errorTextView,
                binding.retryButton,
                binding.skeletonLoading
            )
            it.show(adapter)
        }

        viewModel.reload()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            viewModel.restore(BundleWrapper.Base(it))
        }
    }
}