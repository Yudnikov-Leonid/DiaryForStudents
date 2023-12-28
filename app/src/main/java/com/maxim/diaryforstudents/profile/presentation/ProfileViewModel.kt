package com.maxim.diaryforstudents.profile.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.profile.data.ProfileRepository

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val communication: ProfileCommunication.Mutable,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<ProfileState>, ShowProfileCallback {
    fun init() {
        communication.update(ProfileState.Loading)
        handle({ repository.data(this) }) {}
    }

    override fun show(name: String, grade: String, email: String) {
        communication.update(ProfileState.Base(name, grade, email))
    }

    fun signOut() {
        repository.signOut()
        navigation.update(LoginScreen)
        clear.clearViewModel(ProfileViewModel::class.java)
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(ProfileViewModel::class.java)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ProfileState>) {
        communication.observe(owner, observer)
    }
}

interface ShowProfileCallback {
    fun show(name: String, grade: String, email: String)
}