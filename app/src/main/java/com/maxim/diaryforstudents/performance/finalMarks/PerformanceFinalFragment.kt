package com.maxim.diaryforstudents.performance.finalMarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentFinalPerformanceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerformanceFinalFragment: BaseFragment<FragmentFinalPerformanceBinding>() {
    private val viewModel: PerformanceFinalViewModel by viewModels()
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFinalPerformanceBinding.inflate(inflater, container, false)

    override var setOnBackPressedCallback = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PerformanceFinalLessonsAdapter(viewModel.colorManager)
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