package com.maxim.diaryforstudents.menu.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface MenuCommunication : Communication.All<MenuState> {
    class Base : Communication.RegularWithDeath<MenuState>(), MenuCommunication
}