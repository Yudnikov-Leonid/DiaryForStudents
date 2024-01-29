package com.maxim.diaryforstudents.performance.actualMarks.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentActualPerformanceBinding
import com.maxim.diaryforstudents.performance.presentation.PerformanceLessonsAdapter
import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

class PerformanceActualFragment: BaseFragment<FragmentActualPerformanceBinding, PerformanceActualViewModel>() {
    override val viewModelClass = PerformanceActualViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentActualPerformanceBinding.inflate(inflater, container, false)

    private var spinnerLastPosition = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PerformanceLessonsAdapter(object : PerformanceLessonsAdapter.Listener {
            override fun calculate(marks: List<PerformanceUi.Mark>, marksSum: Int) {
                viewModel.calculateAverage(marks, marksSum)
            }
        })

        binding.retryButton.setOnClickListener {
            viewModel.init(true)
        }

        binding.lessonsRecyclerView.adapter = adapter
        binding.lessonsRecyclerView.itemAnimator = null
        var scrolledUp = true
        var scrollDy = 0
        binding.lessonsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollDy += dy
                if (scrollDy < 300 && !scrolledUp) {
                    binding.settingsBar.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.trans_downwards
                        )
                    )
                    scrolledUp = true
                } else if (scrollDy  > 300 && scrolledUp) {
                    binding.settingsBar.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.trans_upwards
                        )
                    )
                    scrolledUp = false
                }
            }
        })

        val spinnerAdapter = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != spinnerLastPosition && spinnerLastPosition != -1) {
                    viewModel.changeQuarter(position + 1)
                }

                spinnerLastPosition = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        viewModel.observe(this) {
            binding.quarterSpinner.onItemSelectedListener = null
            it.show(
                binding.quarterSpinner,
                binding.settingsBar,
                adapter,
                binding.errorTextView,
                binding.retryButton,
                binding.progressBar,
            )
            binding.quarterSpinner.onItemSelectedListener = spinnerAdapter

        }

        binding.settingsImageButton.setOnClickListener {
            viewModel.settings()
        }

        viewModel.init(savedInstanceState == null)
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