package com.maxim.diaryforstudents.settings.themes

import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface SettingsThemesCommunication: Communication.Mutable<SettingsThemesState> {
    class Base: Communication.Regular<SettingsThemesState>(MutableStateFlow(SettingsThemesState.Empty)), SettingsThemesCommunication
}