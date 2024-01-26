package com.maxim.diaryforstudents.profile.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.eduLogin.presentation.EduLoginScreen
import com.maxim.diaryforstudents.profile.eduData.EduProfileRepository

class ProfileViewModel(
    private val repository: EduProfileRepository,
    private val communication: ProfileCommunication,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<ProfileState> {
    fun init() {
        communication.update(ProfileState.Base(repository.data().toUi()))
    }

    fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(RESTORE_KEY, bundleWrapper)
    }

    fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(RESTORE_KEY, bundleWrapper)
    }

    fun signOut() {
        repository.signOut()
        navigation.update(EduLoginScreen)
        clear.clearViewModel(ProfileViewModel::class.java)
    }

    fun back() {
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