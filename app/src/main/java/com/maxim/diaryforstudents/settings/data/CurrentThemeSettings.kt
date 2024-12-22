package com.maxim.diaryforstudents.settings.data

import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.data.SimpleStorage

interface CurrentThemeSettings {
    fun setTheme(theme: CurrentTheme)
    fun readTheme(): CurrentTheme

    class Base(private val simpleStorage: SimpleStorage) : CurrentThemeSettings {
        override fun setTheme(theme: CurrentTheme) {
            simpleStorage.save(KEY, theme.getId())
        }

        override fun readTheme(): CurrentTheme {
            val allThemes = listOf(CurrentTheme.Default, CurrentTheme.NewYear)
            val savedId = simpleStorage.read(KEY, 0)
            allThemes.forEach {
                if (it.getId() == savedId) return  it
            }
            return CurrentTheme.Default
        }

        companion object {
            private const val KEY = "current_theme_key"
        }
    }
}

interface CurrentTheme {
    fun getMenuLayoutId(): Int
    fun getId(): Int

    object Default : CurrentTheme {
        override fun getMenuLayoutId() = R.layout.fragment_menu
        override fun getId() = 0
    }

    object NewYear : CurrentTheme {
        override fun getMenuLayoutId() = R.layout.fragment_menu_ny
        override fun getId() = 1
    }
}