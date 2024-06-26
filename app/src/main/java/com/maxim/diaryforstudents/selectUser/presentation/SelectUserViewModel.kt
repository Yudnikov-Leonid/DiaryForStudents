package com.maxim.diaryforstudents.selectUser.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.presentation.SimpleInit
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.menu.presentation.MenuScreen
import com.maxim.diaryforstudents.selectUser.sl.SelectUserModule

class SelectUserViewModel(
    private val repository: LoginRepository,
    private val communication: SelectUserCommunication,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
    private val module: SelectUserModule
): ViewModel(), GoBack, SimpleInit, Communication.Observe<SelectUserState>, SaveAndRestore {

    override fun init() {
        communication.update(SelectUserState.Base(repository.users()))
    }

    fun select(position: Int) {
        repository.select(position)
        module.clear()
        navigation.update(MenuScreen)
        clearViewModel.clearViewModel(LoginViewModel::class.java)
        clearViewModel.clearViewModel(SelectUserViewModel::class.java)
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clearViewModel(SelectUserViewModel::class.java)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<SelectUserState>) {
        communication.observe(owner, observer)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        repository.save(bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        repository.restore(bundleWrapper)
    }
}