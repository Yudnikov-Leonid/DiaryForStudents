package com.maxim.diaryforstudents.lessonDetails.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentLessonDetailsBinding

class LessonDetailsFragment: BaseFragment<FragmentLessonDetailsBinding, LessonDetailsViewModel>() {
    override val viewModelClass = LessonDetailsViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLessonDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(it)) }

        viewModel.show(
            binding.lessonNameTextView,
            binding.teacherNameTextView,
            binding.topicTextView,
            binding.topicTitleTextView,
            binding.homeworkTextView,
            binding.homeworkTitleTextView,
            binding.previousHomeworkTextView,
            binding.previousHomeworkTitleTextView,
            binding.marksLayout,
            binding.noteTitleTextView,
            binding.noteTextView
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }
}