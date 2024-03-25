package com.maxim.diaryforstudents.core.service

import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.performance.common.room.PerformanceDao

interface EduUser {
    fun login(
        guid: String,
        apikey: String,
        email: String,
        fullName: String,
        schoolName: String,
        grade: String,
        gradeHeadName: String
    )

    fun name(): String
    fun email(): String
    fun school(): String
    fun grade(): Pair<String, String>
    fun guid(): String
    fun apikey(): String
    fun isLogged(): Boolean
    suspend fun signOut()

    class Base(private val simpleStorage: SimpleStorage, private val dao: PerformanceDao) : EduUser {
        override fun login(
            guid: String,
            apikey: String,
            email: String,
            fullName: String,
            schoolName: String,
            grade: String,
            gradeHeadName: String
        ) {
            simpleStorage.save(GUID_KEY, guid)
            simpleStorage.save(APIKEY_KEY, apikey)
            simpleStorage.save(EMAIL_KEY, email)
            simpleStorage.save(FULL_NAME_KEY, fullName)
            simpleStorage.save(SCHOOL_NAME_KEY, schoolName)
            simpleStorage.save(GRADE_KEY, grade)
            simpleStorage.save(GRADE_HEAD_KEY, gradeHeadName)
        }

        override fun name() = simpleStorage.read(FULL_NAME_KEY, "Something went wrong")

        override fun email() = simpleStorage.read(EMAIL_KEY, "Something went wrong")

        override fun school() = simpleStorage.read(SCHOOL_NAME_KEY, "Something went wrong")

        override fun grade() =
            Pair(
                simpleStorage.read(GRADE_KEY, "Something went wrong"), simpleStorage.read(
                    GRADE_HEAD_KEY, "Something went wrong"
                )
            )

        override fun guid() = simpleStorage.read(GUID_KEY, "")
        override fun apikey() = simpleStorage.read(APIKEY_KEY, "")

        override fun isLogged() = simpleStorage.read(GUID_KEY, "") != ""
        override suspend fun signOut() {
            simpleStorage.save(GUID_KEY, "")
            dao.clearAll()
        }

        companion object {
            private const val GUID_KEY = "guid"
            private const val APIKEY_KEY = "apikey"
            private const val EMAIL_KEY = "email"
            private const val FULL_NAME_KEY = "full_name"
            private const val SCHOOL_NAME_KEY = "school_name"
            private const val GRADE_KEY = "grade"
            private const val GRADE_HEAD_KEY = "grade_head"
        }
    }
}