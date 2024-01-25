package com.maxim.diaryforstudents.eduLogin.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentEduLoginBinding

class EduLoginFragment : BaseFragment<FragmentEduLoginBinding, EduLoginViewModel>() {
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

        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.loginInputEditText.text.toString(),
                binding.passwordInputEditText.text.toString()
            )
        }

        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.loginInputLayout.error = ""
                binding.loginInputLayout.isErrorEnabled = false
                binding.passwordInputLayout.error = ""
                binding.passwordInputLayout.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }

        binding.loginInputEditText.addTextChangedListener(listener)
        binding.passwordInputEditText.addTextChangedListener(listener)

        viewModel.init(savedInstanceState == null)
    }
}