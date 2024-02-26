package com.maxim.diaryforstudents.settings.themes

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.settings.data.SettingsThemesRepository

class SettingsThemesViewModel(
    private val repository: SettingsThemesRepository,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : ViewModel(), GoBack {

    fun openColorPicker(key: String, defaultColor: Int, openColorPicker: OpenColorPicker) {
        if (repository.hasColor(key))
            openColorPicker.open(repository.defaultColor(key, 0), false, key)
        else
            openColorPicker.open(defaultColor, true, key)
    }

    fun resetColor(key: String) {
        repository.resetColor(key)
    }

    fun showDefaultColors(
        fiveImageView: ImageView,
        fourImageView: ImageView,
        threeImageView: ImageView,
        twoImageView: ImageView,
        oneImageView: ImageView
    ) {
        if (repository.hasColor("5"))
            fiveImageView.setBackgroundColor(repository.defaultColor("5", 0))
        else
            fiveImageView.setBackgroundColor(
                ContextCompat.getColor(
                    fiveImageView.context,
                    R.color.light_green
                )
            )

        if (repository.hasColor("4"))
            fourImageView.setBackgroundColor(repository.defaultColor("4", 0))
        else
            fourImageView.setBackgroundColor(
                ContextCompat.getColor(
                    fiveImageView.context,
                    R.color.green
                )
            )

        if (repository.hasColor("3"))
            threeImageView.setBackgroundColor(repository.defaultColor("3", 0))
        else
            threeImageView.setBackgroundColor(
                ContextCompat.getColor(
                    fiveImageView.context,
                    R.color.yellow
                )
            )

        if (repository.hasColor("2"))
            twoImageView.setBackgroundColor(repository.defaultColor("2", 0))
        else
            twoImageView.setBackgroundColor(
                ContextCompat.getColor(
                    fiveImageView.context,
                    R.color.red
                )
            )

        if (repository.hasColor("1"))
            oneImageView.setBackgroundColor(repository.defaultColor("1", 0))
        else
            oneImageView.setBackgroundColor(
                ContextCompat.getColor(
                    fiveImageView.context,
                    R.color.red
                )
            )
    }

    fun saveColor(color: Int, key: String) {
        repository.saveColor(color, key)
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clearViewModel(SettingsThemesViewModel::class.java)
    }
}