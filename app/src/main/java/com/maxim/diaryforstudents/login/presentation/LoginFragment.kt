package com.maxim.diaryforstudents.login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.sl.ProvideViewModel
import com.maxim.diaryforstudents.databinding.FragmentLoginBinding
import com.maxim.diaryforstudents.login.data.AuthResultWrapper

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val viewModelClass: Class<LoginViewModel>
        get() = LoginViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoginBinding.inflate(inflater, container, false)

    private val authResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.handleResult(AuthResultWrapper.Base(it))
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ProvideViewModel).viewModel(LoginViewModel::class.java)

        viewModel.observe(this) {
            it.show(binding.loginButton, binding.progressBar, binding.errorTextView)
            it.handle(authResult, requireActivity())
        }

        binding.loginButton.setOnClickListener {
            viewModel.login()
        }

        viewModel.init(savedInstanceState == null)
    }
}