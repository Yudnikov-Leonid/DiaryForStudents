package com.maxim.diaryforstudents.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface LoginCommunication {
    interface Read {
        fun loginErrorMessage(): LiveData<String>
        fun passwordErrorMessage(): LiveData<String>
        fun errorMessage(): LiveData<String>
        fun isLoading(): LiveData<Boolean>
    }

    interface Write {
        fun setLoginErrorMessage(value: String)
        fun setPasswordErrorMessage(value: String)
        fun setErrorMessage(value: String)
        fun setLoading(value: Boolean)
    }

    interface Mutable: Read, Write

    class Base: Mutable {
        private val loginErrorLiveData = MutableLiveData<String>()
        private val passwordErrorLiveData = MutableLiveData<String>()
        private val errorLiveData = MutableLiveData<String>()
        private val loadingLiveData = MutableLiveData<Boolean>()

        override fun loginErrorMessage() = loginErrorLiveData

        override fun passwordErrorMessage() = passwordErrorLiveData
        override fun errorMessage() = errorLiveData
        override fun isLoading() = loadingLiveData

        override fun setLoginErrorMessage(value: String) {
            loginErrorLiveData.value = value
        }

        override fun setPasswordErrorMessage(value: String) {
            passwordErrorLiveData.value = value
        }

        override fun setErrorMessage(value: String) {
            errorLiveData.value = value
        }

        override fun setLoading(value: Boolean) {
            loadingLiveData.value = value
        }
    }
}