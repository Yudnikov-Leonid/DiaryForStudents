package com.maxim.diaryforstudents.settings.presentation

import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.settings.themes.SettingsThemesScreen
import com.maxim.diaryforstudents.settings.utilities.UtilitiesScreen

class SettingsViewModel(
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
): ViewModel(), GoBack {

    fun themes() {
        navigation.update(SettingsThemesScreen)
    }

    fun utilities() {
        navigation.update(UtilitiesScreen)
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clearViewModel(SettingsViewModel::class.java)
    }
}