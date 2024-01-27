package com.maxim.diaryforstudents.performance.domain

interface Failure {
    fun message(): String
}

class NoInternetConnectionError: Failure {
    override fun message() = "No internet connection"
}

class ServiceUnavailableError(private val message: String): Failure {
    override fun message() = message
}

class UnknownError: Failure {
    override fun message() = "Unknown error"
}