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

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val viewModelClass = LoginViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding.loginButton.setOnClickListener {
//            viewModel.login(
//                binding.loginInputEditText.text.toString(),
//                binding.passwordInputEditText.text.toString(),
//                requireActivity() as HideKeyboard
//            )
//        }

        binding.loginInputEditText.addTextChangedListener {
            viewModel.hideErrors()
        }
        binding.passwordInputEditText.addTextChangedListener {
            viewModel.hideErrors()
        }

        binding.contactsTextView.movementMethod = LinkMovementMethod.getInstance()
        binding.contactsTextView.text =
            Html.fromHtml(resources.getString(R.string.contacts), Html.FROM_HTML_MODE_LEGACY)

        viewModel.init(savedInstanceState == null)
    }
}