package com.maxim.diaryforstudents.settings.themes

import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.ColorManager

interface SettingsThemesState {

    fun show(
        fiveImageButton: ImageButton,
        fourImageButton: ImageButton,
        threeImageButton: ImageButton,
        twoImageButton: ImageButton,
        oneImageButton: ImageButton,
        colorManager: ColorManager,
        themesLayout: LinearLayout
    )

    data class Base(
        private val defaultFiveColor: Int,
        private val defaultFourColor: Int,
        private val defaultThreeColor: Int,
        private val defaultTwoColor: Int,
        private val defaultOneColor: Int,
        private val currentThemeId: Int
    ): SettingsThemesState {

        override fun show(
            fiveImageButton: ImageButton,
            fourImageButton: ImageButton,
            threeImageButton: ImageButton,
            twoImageButton: ImageButton,
            oneImageButton: ImageButton,
            colorManager: ColorManager,
            themesLayout: LinearLayout
        ) {
            colorManager.showColor(fiveImageButton, "5", defaultFiveColor)
            colorManager.showColor(fourImageButton, "4", defaultFourColor)
            colorManager.showColor(threeImageButton, "3", defaultThreeColor)
            colorManager.showColor(twoImageButton, "2", defaultTwoColor)
            colorManager.showColor(oneImageButton, "1", defaultOneColor)
            themesLayout.children.forEachIndexed { i, view ->
                view.setBackgroundColor(ContextCompat.getColor(themesLayout.context, if (i == currentThemeId) R.color.gray else R.color.white))
            }
        }
    }
}