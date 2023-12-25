package com.maxim.diaryforstudents.profile

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.BaseViewModel
import com.maxim.diaryforstudents.core.ClearViewModel
import com.maxim.diaryforstudents.core.Communication
import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.core.RunAsync
import com.maxim.diaryforstudents.core.Screen
import com.maxim.diaryforstudents.login.presentation.LoginScreen

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val communication: ProfileCommunication.Mutable,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<ProfileState> {
    fun init() {
        communication.update(ProfileState.Loading)
        handle({ repository.data() }) {
            communication.update(ProfileState.Base(it.first, it.second, it.third))
        }
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