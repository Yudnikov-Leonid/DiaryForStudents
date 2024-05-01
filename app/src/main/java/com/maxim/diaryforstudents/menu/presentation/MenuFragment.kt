package com.maxim.diaryforstudents.menu.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentMenuBinding
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>() {
    private val viewModel: MenuViewModel by viewModels()

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMenuBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.diaryButton.setOnClickListener {
            viewModel.diary()
        }
        binding.performanceButton.setOnClickListener {
            viewModel.performance()
        }
        binding.profileButton.setOnClickListener {
            viewModel.profile()
        }
        binding.newsButton.setOnClickListener {
            viewModel.news()
            viewModel.reload()
        }
        binding.analyticsButton.setOnClickListener {
            viewModel.analytics()
        }
        binding.settingsButton.setOnClickListener {
            viewModel.settings()
        }

        val adapter = MenuLessonsAdapter(object : MenuLessonsAdapter.Listener {
            override fun details(item: DiaryUi.Lesson) {
                viewModel.lesson(item)
            }
        })
        binding.lessonsViewPager.adapter = adapter

        viewModel.observe(this) {
            it.showNewsCount(binding.newNewsCounter)
            it.showMarksCount(binding.newMarksCounter)
            it.showLessons(binding.lessonsViewPager, adapter)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateLessons()
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