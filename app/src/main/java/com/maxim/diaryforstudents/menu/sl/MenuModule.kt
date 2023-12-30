package com.maxim.diaryforstudents.menu.sl

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.menu.data.MenuRepository
import com.maxim.diaryforstudents.menu.domain.MenuInteractor
import com.maxim.diaryforstudents.menu.presentation.MenuCommunication
import com.maxim.diaryforstudents.menu.presentation.MenuViewModel

class MenuModule(private val core: Core) : Module<MenuViewModel> {
    override fun viewModel() = MenuViewModel(
        MenuInteractor.Base(MenuRepository.Base(core.dataBase())),
        MenuCommunication.Base(),
        core.navigation()
    )
}