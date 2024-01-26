package com.maxim.diaryforstudents.core.service

import com.maxim.diaryforstudents.core.data.SimpleStorage

interface EduUser {
    fun login(guid: String, fullName: String, schoolName: String, grade: String)
    fun guid(): String

    class Base(private val simpleStorage: SimpleStorage): EduUser {
        override fun login(guid: String, fullName: String, schoolName: String, grade: String) {
            simpleStorage.save(GUID_KEY, guid)
            simpleStorage.save(FULL_NAME_KEY, fullName)
            simpleStorage.save(SCHOOL_NAME_KEY, schoolName)
            simpleStorage.save(GRADE_KEY, grade)
        }

        override fun guid() = simpleStorage.read(GUID_KEY, "")

        companion object {
            private const val GUID_KEY = "guid"
            private const val FULL_NAME_KEY = "full_name"
            private const val SCHOOL_NAME_KEY = "school_name"
            private const val GRADE_KEY = "grade"
        }
    }
}