package com.maxim.diaryforstudents.menu

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module

class MenuModule(private val core: Core) : Module<MenuViewModel> {
    override fun viewModel() = MenuViewModel(core.navigation())
}