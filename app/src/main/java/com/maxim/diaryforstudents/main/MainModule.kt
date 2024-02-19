package com.maxim.diaryforstudents.main

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module

class MainModule(private val core: Core) : Module<MainViewModel> {
    override fun viewModel() =
        MainViewModel(
            MainInteractor.Base(core.eduUser()),
            core.navigation()
        )
}