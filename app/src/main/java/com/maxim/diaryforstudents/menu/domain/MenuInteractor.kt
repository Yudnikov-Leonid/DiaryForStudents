package com.maxim.diaryforstudents.menu.domain

import com.maxim.diaryforstudents.menu.data.MenuRepository

interface MenuInteractor {
    suspend fun userStatus(): UserStatus

    class Base(private val repository: MenuRepository): MenuInteractor {
        override suspend fun userStatus() = repository.getUserStatus()
    }
}