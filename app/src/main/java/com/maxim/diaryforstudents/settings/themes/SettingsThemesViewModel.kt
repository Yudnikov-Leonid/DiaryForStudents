package com.maxim.diaryforstudents.settings.themes

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.settings.data.SettingsThemesRepository

class SettingsThemesViewModel(
    private val communication: SettingsThemesCommunication,
    private val repository: SettingsThemesRepository,
    private val defaultColors: List<Int>,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : ViewModel(), GoBack, Communication.Observe<SettingsThemesState>, Reload {

    fun openColorPicker(key: String, defaultColor: Int, openColorPicker: OpenColorPicker) {
        if (repository.hasColor(key))
            openColorPicker.open(repository.defaultColor(key, 0), false, key)
        else
            openColorPicker.open(defaultColor, true, key)
    }

    fun resetColor(key: String) {
        repository.resetColor(key)
        reload()
    }

    override fun reload() {
        communication.update(SettingsThemesState.Base(
            defaultColors[0],
            defaultColors[1],
            defaultColors[2],
            defaultColors[3],
            defaultColors[4],
        ))
    }

    fun saveColor(color: Int, key: String) {
        repository.saveColor(color, key)
        reload()
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clearViewModel(SettingsThemesViewModel::class.java)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<SettingsThemesState>) {
        communication.observe(owner, observer)
    }
}