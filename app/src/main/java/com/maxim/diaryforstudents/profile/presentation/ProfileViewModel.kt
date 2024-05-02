package com.maxim.diaryforstudents.profile.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.presentation.SimpleInit
import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.profile.data.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val communication: ProfileCommunication,
    private val navigation: Navigation.Update,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<ProfileState>, SimpleInit, GoBack, SaveAndRestore {
    override fun init() {
        communication.update(ProfileState.Base(repository.name()))
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(RESTORE_KEY, bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(RESTORE_KEY, bundleWrapper)
    }

    fun signOut() {
        handle {
            repository.signOut()
        }
        navigation.update(LoginScreen)
    }

    fun email(showEmail: ShowEmail) {
        showEmail.showEmail(repository.email())
    }

    fun school(showSchoolInfo: ShowSchoolInfo) {
        showSchoolInfo.showSchool(repository.school())
    }

    fun grade(showGradeInfo: ShowGradeInfo) {
        val data = repository.grade()
        showGradeInfo.showGrade(data.first, data.second)
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ProfileState>) {
        communication.observe(owner, observer)
    }

    companion object {
        private const val RESTORE_KEY = "profile_communication_restore"
    }
}