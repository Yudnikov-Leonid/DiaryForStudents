package com.maxim.diaryforstudents.login.data

interface LoginResult {
    fun isSuccessful(): Boolean
    fun message(): String

    object Success: LoginResult {
        override fun isSuccessful() = true

        override fun message() = ""
    }

    data class Failure(private val message: String): LoginResult {
        override fun isSuccessful() = false

        override fun message() = message
    }
}