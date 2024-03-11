package com.maxim.diaryforstudents.profile.data

import com.maxim.diaryforstudents.core.service.EduUser

interface ProfileRepository {
    fun name(): String
    fun email(): String
    fun school(): String
    fun grade(): Pair<String, String>
    suspend fun signOut()

    class Base(private val eduUser: EduUser): ProfileRepository {
        override fun name() = eduUser.name()

        override fun email() = eduUser.email()

        override fun school() = eduUser.school()

        override fun grade() = eduUser.grade()

        override suspend fun signOut() = eduUser.signOut()
    }
}