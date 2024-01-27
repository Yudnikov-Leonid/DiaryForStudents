package com.maxim.diaryforstudents.profile.data

import com.maxim.diaryforstudents.core.service.EduUser

interface ProfileRepository {
    fun data(): ProfileData
    fun signOut()

    class Base(private val eduUser: EduUser): ProfileRepository {
        override fun data() = eduUser.profileData()
        override fun signOut() = eduUser.signOut()
    }
}