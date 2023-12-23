package com.maxim.diaryforstudents.login

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.ManageResource

interface LoginState {
    fun handle(launcher: ActivityResultLauncher<Intent>, activity: Activity) = Unit
    fun show(loginButton: SignInButton, progressBar: ProgressBar, errorTextView: TextView) = Unit

    object Initial : LoginState {
        override fun show(
            loginButton: SignInButton,
            progressBar: ProgressBar,
            errorTextView: TextView
        ) {
            loginButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            errorTextView.text = ""
        }
    }

    data class Failed(private val message: String) : LoginState {
        override fun show(
            loginButton: SignInButton,
            progressBar: ProgressBar,
            errorTextView: TextView
        ) {
            loginButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            errorTextView.text = message
        }
    }

    data class Auth(private val resourceManager: ManageResource) : LoginState {
        private val options by lazy {
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resourceManager.string(R.string.client_web_id))
                .requestEmail().build()
        }

        override fun handle(launcher: ActivityResultLauncher<Intent>, activity: Activity) {
            launcher.launch(GoogleSignIn.getClient(activity, options).signInIntent)
        }

        override fun show(
            loginButton: SignInButton,
            progressBar: ProgressBar,
            errorTextView: TextView
        ) {
            loginButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            errorTextView.text = ""
        }
    }
}