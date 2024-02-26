package com.maxim.diaryforstudents.core.presentation

import com.maxim.diaryforstudents.core.data.SimpleStorage

interface ColorManager {
    fun saveColor(color: Int, key: String)
    fun getColor(key: String, default: Int): Int
    fun hasColor(key: String): Boolean
    fun resetColor(key: String)

    class Base(private val simpleStorage: SimpleStorage): ColorManager {
        override fun saveColor(color: Int, key: String) {
            simpleStorage.save("$KEY$key", color)
        }

        override fun getColor(key: String, default: Int): Int = simpleStorage.read("$KEY$key", default)

        override fun hasColor(key: String) = simpleStorage.read("$KEY$key", -1) != -1

        override fun resetColor(key: String) {
            simpleStorage.save("$KEY$key", -1)
        }

        companion object {
            private const val KEY = "color_"
        }
    }
}