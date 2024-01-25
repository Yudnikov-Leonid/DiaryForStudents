package com.maxim.diaryforstudents.eduLogin.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentEduLoginBinding

class EduLoginFragment: BaseFragment<FragmentEduLoginBinding, EduLoginViewModel>() {
    override val viewModelClass = EduLoginViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEduLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(this) {
            it.show(
                binding.loginInputLayout,
                binding.passwordInputLayout,
                binding.loginButton,
                binding.progressBar,
                binding.errorTextView
            )
        }

        viewModel.init(savedInstanceState == null)
    }
}