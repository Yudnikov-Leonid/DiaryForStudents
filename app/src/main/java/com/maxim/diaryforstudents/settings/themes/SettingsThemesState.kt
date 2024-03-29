package com.maxim.diaryforstudents.settings.themes

import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
import com.maxim.diaryforstudents.core.presentation.ColorManager

interface SettingsThemesState {

    fun show(
        fiveImageButton: ImageButton,
        fourImageButton: ImageButton,
        threeImageButton: ImageButton,
        twoImageButton: ImageButton,
        oneImageButton: ImageButton,
        colorManager: ColorManager,
        showLessonsSwitch: SwitchCompat
    )

    data class Base(
        private val defaultFiveColor: Int,
        private val defaultFourColor: Int,
        private val defaultThreeColor: Int,
        private val defaultTwoColor: Int,
        private val defaultOneColor: Int,
        private val showLessonsInMenu: Boolean
    ): SettingsThemesState {

        override fun show(
            fiveImageButton: ImageButton,
            fourImageButton: ImageButton,
            threeImageButton: ImageButton,
            twoImageButton: ImageButton,
            oneImageButton: ImageButton,
            colorManager: ColorManager,
            showLessonsSwitch: SwitchCompat
        ) {
            colorManager.showColor(fiveImageButton, "5", defaultFiveColor)
            colorManager.showColor(fourImageButton, "4", defaultFourColor)
            colorManager.showColor(threeImageButton, "3", defaultThreeColor)
            colorManager.showColor(twoImageButton, "2", defaultTwoColor)
            colorManager.showColor(oneImageButton, "1", defaultOneColor)
            showLessonsSwitch.isChecked = showLessonsInMenu
        }
    }
}