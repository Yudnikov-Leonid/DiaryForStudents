package com.maxim.diaryforstudents.profile.eduData

import com.maxim.diaryforstudents.core.service.EduUser

interface EduProfileRepository {
    fun data(): EduProfileData
    fun signOut()

    class Base(private val eduUser: EduUser): EduProfileRepository {
        override fun data() = eduUser.profileData()
        override fun signOut() = eduUser.signOut()
    }
}