package com.maxim.diaryforstudents.main

import com.maxim.diaryforstudents.core.service.EduUser
import javax.inject.Inject

interface MainInteractor {
    fun isLogged(): Boolean

    class Base @Inject constructor(private val eduUser: EduUser): MainInteractor {
        override fun isLogged() = eduUser.isLogged()
    }
}