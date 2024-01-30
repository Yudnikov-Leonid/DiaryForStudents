package com.maxim.diaryforstudents.performance.analytics.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentAnalyticsBinding

class AnalyticsFragment : BaseFragment<FragmentAnalyticsBinding, AnalyticsViewModel>() {
    override val viewModelClass = AnalyticsViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAnalyticsBinding.inflate(inflater, container, false)

    private val isDependent = arguments?.getBoolean(INDEPENDENT_KEY) ?: true

    override var setOnBackPressedCallback = isDependent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }

        super.onViewCreated(view, savedInstanceState)

        val adapter = AnalyticsAdapter(object : AnalyticsAdapter.Listener {
            override fun changeQuarter(value: Int) {
                viewModel.changeQuarter(value)
            }
        })
        binding.lessonsRecyclerView.adapter = adapter

        binding.retryButton.setOnClickListener {
            viewModel.reload()
        }

        viewModel.observe(this) {
            it.show(
                binding.titleTextView,
                adapter,
                binding.progressBar,
                binding.errorTextView,
                binding.retryButton
            )
        }

        viewModel.init(savedInstanceState == null, isDependent)
    }

    companion object {
        private const val INDEPENDENT_KEY = "independent_key"

        fun newInstance(isIndependent: Boolean): AnalyticsFragment {
            return AnalyticsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(INDEPENDENT_KEY, isIndependent)
                }
            }
        }
    }
}