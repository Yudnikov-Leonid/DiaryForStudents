package com.maxim.diaryforstudents.performance.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentPerformanceBinding
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class PerformanceFragment : BaseFragment<FragmentPerformanceBinding, PerformanceViewModel>() {
    override val viewModelClass = PerformanceViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPerformanceBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.back()
                binding.searchEditText.setText("")
            }
        }
        super.onViewCreated(view, savedInstanceState)

        val adapter = PerformanceLessonsAdapter()
        binding.lessonsRecyclerView.adapter = adapter
        viewModel.observe(this) {
            it.show(
                binding.quarterButtonLayout,
                binding.firstQuarterButton,
                binding.secondQuarterButton,
                binding.thirdQuarterButton,
                binding.fourthQuarterButton,
                binding.actualGradesButton,
                binding.finalGradesButton,
                adapter,
                binding.errorTextView,
                binding.retryButton!!,
                binding.progressBar,
                binding.searchEditText
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
        binding.actualGradesButton.setOnClickListener {
            viewModel.changeType(MarksType.Base)
        }
        binding.finalGradesButton.setOnClickListener {
            viewModel.changeType(MarksType.Final)
        }
        binding.searchEditText.addTextChangedListener {
            viewModel.search(binding.searchEditText.text.toString())
        }
        binding.retryButton!!.setOnClickListener {
            viewModel.init(true)
        }
        KeyboardVisibilityEvent.setEventListener(requireActivity(), this) { isOpen ->
            if (!isOpen)
                binding.searchEditText.clearFocus()
        }

        viewModel.init(savedInstanceState == null)
    }
}