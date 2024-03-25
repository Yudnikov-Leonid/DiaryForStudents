package com.maxim.diaryforstudents.selectUser.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentSelectUserBinding
import com.maxim.diaryforstudents.databinding.SelectUserLayoutBinding

class SelectUserFragment : BaseFragment<FragmentSelectUserBinding, SelectUserViewModel>(),
    ShowUsersToSelect {
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

        binding.backButton.setOnClickListener {
            viewModel.goBack()
        }

        viewModel.observe(this) {
            it.show(this)
        }

        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(it)) }
        viewModel.init()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun show(items: List<SelectUserUi>) {
        if (binding.usersLayout.childCount > 1)
            binding.usersLayout.removeViews(1, binding.usersLayout.childCount)
        items.forEachIndexed { i, it ->
            val view = SelectUserLayoutBinding.inflate(
                LayoutInflater.from(context),
                binding.usersLayout,
                false
            )
            it.showName(view.nameTextView)
            it.showSchool(view.schoolNameTextView)
            view.root.setOnClickListener {
                viewModel.select(i)
            }
            binding.usersLayout.addView(view.root)
        }
    }
}

interface ShowUsersToSelect {
    fun show(items: List<SelectUserUi>)
}