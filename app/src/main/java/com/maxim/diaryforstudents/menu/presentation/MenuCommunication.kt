package com.maxim.diaryforstudents.menu.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface MenuCommunication: Communication.Mutable<MenuState> {
    class Base: Communication.Regular<MenuState>(), MenuCommunication
}