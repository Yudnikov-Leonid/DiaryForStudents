package com.maxim.diaryforstudents.eduLogin.data

interface EduLoginResult {
    fun isSuccessful(): Boolean
    fun message(): String

    object Success: EduLoginResult {
        override fun isSuccessful() = true

        override fun message() = ""
    }

    data class Failure(private val message: String): EduLoginResult {
        override fun isSuccessful() = false

        override fun message() = message
    }
}