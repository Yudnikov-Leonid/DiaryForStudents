package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentEditDiaryBinding

class EditDiaryFragment : BaseFragment<FragmentEditDiaryBinding, EditDiaryViewModel>() {
    override val viewModelClass = EditDiaryViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEditDiaryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.back()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        val adapter = StudentsAdapter(object : EditGradesAdapter.Listener {
            override fun setGrade(grade: Int?, userId: String, date: Int) {
                viewModel.setGrade(grade, userId, date)
            }

            override fun editLesson(
                date: Int,
                startTime: String,
                endTime: String,
                theme: String,
                homework: String
            ) {
                viewModel.editLesson(date, startTime, endTime, theme, homework)
            }
        })
        binding.recyclerView.adapter = adapter

        binding.newLessonButton.setOnClickListener {
            viewModel.newLesson()
        }

        viewModel.observe(this) {
            it.show(adapter, binding.recyclerView, binding.progressBar, binding.newLessonButton)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(it)) }
    }
}