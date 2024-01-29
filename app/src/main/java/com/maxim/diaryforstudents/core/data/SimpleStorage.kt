package com.maxim.diaryforstudents.core.data

import android.content.SharedPreferences

interface SimpleStorage {
    fun save(key: String, value: String)
    fun read(key: String, defaultValue: String): String

    fun save(key: String, value: Boolean)
    fun read(key: String, defaultValue: Boolean): Boolean

    fun save(key: String, value: Int)
    fun read(key: String, defaultValue: Int): Int

    class Base(private val sharedPreferences: SharedPreferences) : SimpleStorage {
        override fun save(key: String, value: String) {
            sharedPreferences.edit().putString(key, value).apply()
        }

        override fun save(key: String, value: Boolean) {
            sharedPreferences.edit().putBoolean(key, value).apply()
        }

        override fun save(key: String, value: Int) {
            sharedPreferences.edit().putInt(key, value).apply()
        }

        override fun read(key: String, defaultValue: String) =
            sharedPreferences.getString(key, defaultValue) ?: ""

        override fun read(key: String, defaultValue: Boolean) =
            sharedPreferences.getBoolean(key, defaultValue)

        override fun read(key: String, defaultValue: Int) =
            sharedPreferences.getInt(key, defaultValue)
    }
}