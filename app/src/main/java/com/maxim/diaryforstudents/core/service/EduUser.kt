package com.maxim.diaryforstudents.core.service

import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.profile.eduData.EduProfileData

interface EduUser {
    fun login(guid: String, fullName: String, schoolName: String, grade: String)
    fun profileData(): EduProfileData
    fun guid(): String
    fun isLogged(): Boolean

    class Base(private val simpleStorage: SimpleStorage) : EduUser {
        override fun login(guid: String, fullName: String, schoolName: String, grade: String) {
            simpleStorage.save(GUID_KEY, guid)
            simpleStorage.save(FULL_NAME_KEY, fullName)
            simpleStorage.save(SCHOOL_NAME_KEY, schoolName)
            simpleStorage.save(GRADE_KEY, grade)
        }

        override fun profileData() =
            EduProfileData(
                simpleStorage.read(FULL_NAME_KEY, "Something went wrong"),
                simpleStorage.read(SCHOOL_NAME_KEY, ""),
                simpleStorage.read(GRADE_KEY, "")
            )

        override fun guid() = simpleStorage.read(GUID_KEY, "")
        override fun isLogged() = simpleStorage.read(GUID_KEY, "\n") != "\n"

        companion object {
            private const val GUID_KEY = "guid"
            private const val FULL_NAME_KEY = "full_name"
            private const val SCHOOL_NAME_KEY = "school_name"
            private const val GRADE_KEY = "grade"
        }
    }
}