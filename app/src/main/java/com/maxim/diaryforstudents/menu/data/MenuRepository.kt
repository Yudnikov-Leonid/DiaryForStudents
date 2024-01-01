package com.maxim.diaryforstudents.menu.data

import com.maxim.diaryforstudents.core.service.CloudUser
import com.maxim.diaryforstudents.core.service.MyUser
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.menu.domain.UserStatus

interface MenuRepository {
    suspend fun getUserStatus(): UserStatus

    class Base(
        private val service: Service,
        private val myUser: MyUser
    ) : MenuRepository {
        override suspend fun getUserStatus(): UserStatus {
            val user = service.get("users", myUser.id(), CloudUser::class.java)
            return when (user.first().second.status) {
                "student", "" -> UserStatus.Student
                "teacher" -> UserStatus.Teacher
                else -> throw IllegalStateException("unknown user status")
            }
        }
    }
}