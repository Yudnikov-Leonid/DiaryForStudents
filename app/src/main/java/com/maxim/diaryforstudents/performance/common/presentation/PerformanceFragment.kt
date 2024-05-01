package com.maxim.diaryforstudents.performance.common.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentPerformanceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerformanceFragment : BaseFragment<FragmentPerformanceBinding>() {
    private val viewModel: PerformanceCommonViewModel by viewModels()

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPerformanceBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = PerformanceViewPagerAdapter(requireActivity())
        TabLayoutMediator(binding.screenTabLayout, binding.viewPager) { tab, pos ->
            tab.text = when (pos) {
                0 -> resources.getString(R.string.actual)
                else -> resources.getString(R.string.final_marks)
            }
        }.attach()
    }
}