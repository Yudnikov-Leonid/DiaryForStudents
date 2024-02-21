package com.maxim.diaryforstudents.selectUser.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentSelectUserBinding

class SelectUserFragment : BaseFragment<FragmentSelectUserBinding, SelectUserViewModel>() {
    override val viewModelClass = SelectUserViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSelectUserBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        val adapter = SelectUserAdapter(object : SelectUserAdapter.Listener {
            override fun select(position: Int) {
                viewModel.select(position)
            }
        })
        binding.selectUserRecyclerView.adapter = adapter

        viewModel.observe(this) {
            it.show(adapter)
        }

        viewModel.init()
    }
}