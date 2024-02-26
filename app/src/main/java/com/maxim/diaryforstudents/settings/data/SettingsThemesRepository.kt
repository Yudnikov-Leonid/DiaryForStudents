package com.maxim.diaryforstudents.settings.data

import com.maxim.diaryforstudents.core.presentation.ColorManager

interface SettingsThemesRepository {
    fun saveColor(color: Int, key: String)
    fun defaultColor(key: String, default: Int): Int
    fun hasColor(key: String): Boolean
    fun resetColor(key: String)

    class Base(private val colorManager: ColorManager): SettingsThemesRepository {
        override fun saveColor(color: Int, key: String) {
            colorManager.saveColor(color, key)
        }

        override fun defaultColor(key: String, default: Int): Int = colorManager.getColor(key, default)

        override fun hasColor(key: String) = colorManager.hasColor(key)

        override fun resetColor(key: String) = colorManager.resetColor(key)
    }
}