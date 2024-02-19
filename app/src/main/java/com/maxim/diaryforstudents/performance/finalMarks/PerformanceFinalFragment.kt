package com.maxim.diaryforstudents.performance.finalMarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentFinalPerformanceBinding
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceLessonsAdapter
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceMarksAdapter
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

class PerformanceFinalFragment: BaseFragment<FragmentFinalPerformanceBinding, PerformanceFinalViewModel>() {
    override val viewModelClass = PerformanceFinalViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFinalPerformanceBinding.inflate(inflater, container, false)

    override var setOnBackPressedCallback = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PerformanceLessonsAdapter(object : PerformanceLessonsAdapter.Listener {
            override fun calculate(marks: List<PerformanceUi.Mark>, marksSum: Int) = Unit
            override fun analytics(lessonName: String) = Unit
        }, object : PerformanceMarksAdapter.Listener {
            override fun details(mark: PerformanceUi.Mark) = Unit
        })
        binding.lessonsRecyclerView.adapter = adapter

        binding.retryButton.setOnClickListener {
            viewModel.retry()
        }

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