package com.maxim.diaryforstudents.main

import com.maxim.diaryforstudents.core.service.EduUser

interface MainInteractor {
    fun isLogged(): Boolean

    class Base(private val eduUser: EduUser): MainInteractor {
        override fun isLogged() = eduUser.isLogged()
    }
}