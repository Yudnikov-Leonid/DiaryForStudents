package com.maxim.diaryforstudents.menu.presentation

import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface MenuCommunication: Communication.Mutable<MenuState> {
    class Base: Communication.Regular<MenuState>(MutableStateFlow(MenuState.Empty)), MenuCommunication
}