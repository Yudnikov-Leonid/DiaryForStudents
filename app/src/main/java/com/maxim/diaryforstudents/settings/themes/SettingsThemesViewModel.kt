package com.maxim.diaryforstudents.settings.themes

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.settings.data.LessonsInMenuSettings
import com.maxim.diaryforstudents.settings.data.SettingsThemesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsThemesViewModel @Inject constructor(
    private val communication: SettingsThemesCommunication,
    private val repository: SettingsThemesRepository,
    private val showLessons: LessonsInMenuSettings.Mutable,
    private val defaultColors: ListOfColors,
    private val navigation: Navigation.Update,
    val colorManager: ColorManager
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

    fun setShowLessonsInMenu(value: Boolean) {
        showLessons.set(value)
    }

    override fun reload() {
        communication.update(SettingsThemesState.Base(
            defaultColors.list[0],
            defaultColors.list[1],
            defaultColors.list[2],
            defaultColors.list[3],
            defaultColors.list[4],
            showLessons.isShow()
        ))
    }

    fun saveColor(color: Int, key: String) {
        repository.saveColor(color, key)
        reload()
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<SettingsThemesState>) {
        communication.observe(owner, observer)
    }
}

data class ListOfColors(val list: List<Int>)