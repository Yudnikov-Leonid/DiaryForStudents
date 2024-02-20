package com.maxim.diaryforstudents.profile.data

import com.maxim.diaryforstudents.core.service.EduUser

interface ProfileRepository {
    fun name(): String
    fun email(): String
    fun school(): String
    fun grade(): Pair<String, String>
    fun signOut()

    class Base(private val eduUser: EduUser): ProfileRepository {
        override fun name() = eduUser.name()

        override fun email(): String {
            TODO("Not yet implemented")
        }

        override fun school(): String {
            TODO("Not yet implemented")
        }

        override fun grade(): Pair<String, String> {
            TODO("Not yet implemented")
        }

        override fun signOut() = eduUser.signOut()
    }
}