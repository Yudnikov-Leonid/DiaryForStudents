package com.maxim.diaryforstudents.settings.themes

import com.maxim.diaryforstudents.core.presentation.Communication

interface SettingsThemesCommunication: Communication.Mutable<SettingsThemesState> {
    class Base: Communication.Regular<SettingsThemesState>(), SettingsThemesCommunication
}