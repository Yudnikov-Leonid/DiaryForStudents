package com.maxim.diaryforstudents.eduLogin.presentation

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

interface EduLoginState {
    fun show(
        loginInputLayout: TextInputLayout,
        passwordInputLayout: TextInputLayout,
        button: Button,
        progressBar: ProgressBar,
        errorTextView: TextView
    )

    object Initial : EduLoginState {
        override fun show(
            loginInputLayout: TextInputLayout,
            passwordInputLayout: TextInputLayout,
            button: Button,
            progressBar: ProgressBar,
            errorTextView: TextView
        ) {
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
        }
    }

    object Loading : EduLoginState {
        override fun show(
            loginInputLayout: TextInputLayout,
            passwordInputLayout: TextInputLayout,
            button: Button,
            progressBar: ProgressBar,
            errorTextView: TextView
        ) {
            button.isEnabled = false
            progressBar.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
        }
    }

    data class LoginError(private val message: String) : EduLoginState {
        override fun show(
            loginInputLayout: TextInputLayout,
            passwordInputLayout: TextInputLayout,
            button: Button,
            progressBar: ProgressBar,
            errorTextView: TextView
        ) {
            loginInputLayout.isErrorEnabled = true
            loginInputLayout.error = message
            button.isEnabled = true
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
        }
    }

    data class PasswordError(private val message: String) : EduLoginState {
        override fun show(
            loginInputLayout: TextInputLayout,
            passwordInputLayout: TextInputLayout,
            button: Button,
            progressBar: ProgressBar,
            errorTextView: TextView
        ) {
            passwordInputLayout.isErrorEnabled = true
            passwordInputLayout.error = message
            button.isEnabled = true
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
        }
    }

    data class Error(private val message: String): EduLoginState {
        override fun show(
            loginInputLayout: TextInputLayout,
            passwordInputLayout: TextInputLayout,
            button: Button,
            progressBar: ProgressBar,
            errorTextView: TextView
        ) {
            button.isEnabled = true
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = message
        }
    }
}