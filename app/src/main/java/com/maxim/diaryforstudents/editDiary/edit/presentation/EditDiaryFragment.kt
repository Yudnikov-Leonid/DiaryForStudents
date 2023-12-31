package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
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
        })
        binding.recyclerView.adapter = adapter

        viewModel.observe(this) {
            it.show(adapter, binding.progressBar)
        }

        viewModel.init(savedInstanceState == null)
    }
}