package com.maxim.diaryforstudents.main

import com.maxim.diaryforstudents.core.data.SimpleStorage

interface MainInteractor {
    fun isLogged(): Boolean

    class Base(private val simpleStorage: SimpleStorage): MainInteractor {
        override fun isLogged(): Boolean {
            val guid = simpleStorage.read("GUID", "-1")
            return guid != "-1"
        }
    }
}