package com.maxim.diaryforstudents.performance.actualMarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentActualPerformanceBinding
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceLessonsAdapter
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceMarksAdapter
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerformanceActualFragment :
    BaseFragment<FragmentActualPerformanceBinding>() {
    private val viewModel: PerformanceActualViewModel by viewModels()
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentActualPerformanceBinding.inflate(inflater, container, false)

    override var setOnBackPressedCallback = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PerformanceLessonsAdapter(object : PerformanceLessonsAdapter.Listener {
            override fun calculate(marks: List<PerformanceUi>, marksSum: Int) {
                viewModel.calculateAverage(marks, marksSum)
            }

            override fun analytics(lessonName: String) {
                viewModel.analytics(lessonName)
            }
        }, object : PerformanceMarksAdapter.Listener {
            override fun details(mark: PerformanceUi) {
                viewModel.openDetails(mark)
            }
        }, viewModel.colorManager)

        binding.retryButton.setOnClickListener {
            viewModel.retry()
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
                } else if (scrollDy > 300 && scrolledUp) {
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

        var byUser = false
        val spinnerListener = object : AdapterView.OnItemSelectedListener, View.OnTouchListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (byUser)
                    viewModel.changeQuarter(position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                byUser = false
            }

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                byUser = true
                return false
            }
        }
        binding.quarterSpinner.onItemSelectedListener = spinnerListener
        binding.quarterSpinner.setOnTouchListener(spinnerListener)

        viewModel.observe(this) {
            it.show(
                binding.quarterSpinner,
                binding.settingsImageButton,
            )
            it.show(
                binding.errorTextView,
                binding.retryButton,
                binding.skeletonLoading,
            )
            it.show(adapter)
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