package com.maxim.diaryforstudents.login.data

import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.login.LoginCommunication
import com.maxim.diaryforstudents.login.LoginState
import com.maxim.diaryforstudents.profile.ProfileScreen

interface LoginResult {
    fun map(communication: LoginCommunication.Update, navigation: Navigation.Update)
    data class Failed(private val message: String) : LoginResult {
        override fun map(communication: LoginCommunication.Update, navigation: Navigation.Update) {
            communication.update(LoginState.Failed(message))
        }
    }

    object Successful : LoginResult {
        override fun map(communication: LoginCommunication.Update, navigation: Navigation.Update) {
            navigation.update(ProfileScreen)
        }
    }
}