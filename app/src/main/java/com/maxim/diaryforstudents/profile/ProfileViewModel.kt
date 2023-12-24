package com.maxim.diaryforstudents.profile

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.BaseViewModel
import com.maxim.diaryforstudents.core.ClearViewModel
import com.maxim.diaryforstudents.core.Communication
import com.maxim.diaryforstudents.core.ManageResource
import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.core.RunAsync
import com.maxim.diaryforstudents.core.Screen
import com.maxim.diaryforstudents.login.presentation.LoginScreen

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val communication: ProfileCommunication.Mutable,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    private val resources: ManageResource,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<ProfileState> {
    fun init() {
        val data = repository.data()
        val value =
            "${resources.string(R.string.email)}${data.first}\n\n${resources.string(R.string.name)}${data.second}"
        communication.update(ProfileState.Initial(value))
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