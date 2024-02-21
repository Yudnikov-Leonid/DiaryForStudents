package com.maxim.diaryforstudents.login.presentation

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentLoginBinding
import com.maxim.diaryforstudents.main.HideKeyboard

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val viewModelClass = LoginViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoginBinding.inflate(inflater, container, false)

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
                binding.passwordInputEditText.text.toString(),
                requireActivity() as HideKeyboard
            )
        }

        binding.loginInputEditText.addTextChangedListener {
            viewModel.hideError()
        }
        binding.passwordInputEditText.addTextChangedListener {
            viewModel.hideError()
        }

        binding.contactsTextView.movementMethod = LinkMovementMethod.getInstance()
        binding.contactsTextView.text =
            Html.fromHtml(resources.getString(R.string.contacts), Html.FROM_HTML_MODE_LEGACY)

        viewModel.init(savedInstanceState == null)
    }
}