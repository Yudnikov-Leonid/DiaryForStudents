package com.maxim.diaryforstudents.analytics.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentAnalyticsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalyticsFragment : BaseFragment<FragmentAnalyticsBinding>() {

    private val viewModel: AnalyticsViewModel by viewModels()

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAnalyticsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            viewModel.goBack()
        }

        savedInstanceState?.let {
            viewModel.restore(BundleWrapper.Base(it))
        }

        val adapter = AnalyticsAdapter(object : AnalyticsAdapter.Listener {
            override fun changeQuarter(value: Int) {
                viewModel.changeQuarter(value)
            }

            override fun changeInterval(value: Int) {
                viewModel.changeInterval(value)
            }

            override fun goBack() {
                viewModel.goBack()
            }
        })
        binding.lessonsRecyclerView.adapter = adapter
        binding.lessonsRecyclerView.itemAnimator = null

        binding.retryButton.setOnClickListener {
            viewModel.retry()
        }

        viewModel.observe(this) {
            it.show(
                adapter,
                binding.skeletonLoading,
                binding.backButton,
                binding.errorTextView,
                binding.retryButton
            )
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }
}