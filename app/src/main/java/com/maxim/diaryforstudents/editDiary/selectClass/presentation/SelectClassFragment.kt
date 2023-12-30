package com.maxim.diaryforstudents.editDiary.selectClass.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentSelectClassBinding

class SelectClassFragment : BaseFragment<FragmentSelectClassBinding, SelectClassViewModel>() {
    override val viewModelClass = SelectClassViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSelectClassBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.back()
            }
        }
        super.onViewCreated(view, savedInstanceState)
        val adapter = ClassesAdapter(object : ClassesAdapter.Listener {
            override fun openClass(id: String) {
                viewModel.open(id)
            }
        })
        binding.classesRecyclerView.adapter = adapter

        viewModel.observe(this) {
            it.show(adapter, binding.progressBar)
        }

        viewModel.init(savedInstanceState == null)
    }
}