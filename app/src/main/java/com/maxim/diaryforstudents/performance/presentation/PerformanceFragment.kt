package com.maxim.diaryforstudents.performance.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentPerformanceBinding

class PerformanceFragment : BaseFragment<FragmentPerformanceBinding, PerformanceViewModel>() {
    override val viewModelClass = PerformanceViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPerformanceBinding.inflate(inflater, container, false)

    private var spinnerLastPosition = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        val adapter = PerformanceLessonsAdapter(object : PerformanceLessonsAdapter.Listener {
            override fun calculate(marks: List<PerformanceUi.Mark>, marksSum: Int) {
                viewModel.calculateAverage(marks, marksSum)
            }
        })
        binding.lessonsRecyclerView.adapter = adapter
        binding.lessonsRecyclerView.itemAnimator = null
        var scrolledUp = true
        var scrollDy = 0
        binding.lessonsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (scrollDy < 300 && !scrolledUp) {
                    binding.settingsBar.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.trans_downwards
                        )
                    )
                    scrolledUp = true
                } else if (scrollDy > 300 && scrolledUp) {
                    binding.settingsBar.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.trans_upwards
                        )
                    )
                    scrolledUp = false
                }
                scrollDy += dy
            }
        })

        val spinnerAdapter = object : OnItemSelectedListener {
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
        binding.retryButton.setOnClickListener {
            viewModel.init(true)
        }

        binding.screenTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        viewModel.changeType(MarksType.Base)
                        binding.lessonsRecyclerView.scrollToPosition(0)
                    }

                    1 -> {
                        viewModel.changeType(MarksType.Final)
                        binding.lessonsRecyclerView.scrollToPosition(0)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

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