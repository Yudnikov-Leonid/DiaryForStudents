package com.maxim.diaryforstudents.performance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentPerformanceBinding

class PerformanceFragment : BaseFragment<FragmentPerformanceBinding, PerformanceViewModel>() {
    override val viewModelClass: Class<PerformanceViewModel>
        get() = PerformanceViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPerformanceBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.back()
            }
        }
        super.onViewCreated(view, savedInstanceState)
        val adapter = PerformanceLessonsAdapter()
        binding.lessonsRecyclerView.adapter = adapter
        viewModel.observe(this) {
            it.show(
                binding.firstQuarterButton,
                binding.secondQuarterButton,
                binding.thirdQuarterButton,
                binding.fourthQuarterButton,
                adapter,
                requireContext().resources
            )
        }
        binding.firstQuarterButton.setOnClickListener {
            viewModel.changeQuarter(1)
        }
        binding.secondQuarterButton.setOnClickListener {
            viewModel.changeQuarter(2)
        }
        binding.thirdQuarterButton.setOnClickListener {
            viewModel.changeQuarter(3)
        }
        binding.fourthQuarterButton.setOnClickListener {
            viewModel.changeQuarter(4)
        }

        viewModel.init(savedInstanceState == null)
    }
}