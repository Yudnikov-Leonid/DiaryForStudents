package com.maxim.diaryforstudents.profile.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.presentation.SimpleInit
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.profile.data.ProfileData
import com.maxim.diaryforstudents.profile.data.ProfileRepository

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val communication: ProfileCommunication,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    private val mapper: ProfileData.Mapper<ProfileUi>,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<ProfileState>, SimpleInit, GoBack {
    override fun init() {
        communication.update(ProfileState.Base(repository.data().map(mapper)))
    }

    fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(RESTORE_KEY, bundleWrapper)
    }

    fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(RESTORE_KEY, bundleWrapper)
    }

    fun signOut() {
        repository.signOut()
        navigation.update(LoginScreen)
        clear.clearViewModel(ProfileViewModel::class.java)
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(ProfileViewModel::class.java)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ProfileState>) {
        communication.observe(owner, observer)
    }

    companion object {
        private const val RESTORE_KEY = "profile_communication_restore"
    }
}