package com.maxim.diaryforstudents.main

import androidx.lifecycle.LifecycleOwner
import com.maxim.diaryforstudents.core.BaseViewModel
import com.maxim.diaryforstudents.core.Communication
import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.core.Screen
import com.maxim.diaryforstudents.login.LoginScreen

class MainViewModel(
    private val navigation: Navigation.Mutable,
) : BaseViewModel(), Communication.Observe<Screen> {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            navigation.update(LoginScreen)
    }

    override fun observe(owner: LifecycleOwner, observer: androidx.lifecycle.Observer<Screen>) {
        navigation.observe(owner, observer)
    }
}