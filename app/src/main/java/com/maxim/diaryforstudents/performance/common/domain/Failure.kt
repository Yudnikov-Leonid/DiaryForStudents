package com.maxim.diaryforstudents.performance.common.domain

interface Failure {
    fun message(): String
}

class NoInternetConnectionError: Failure {
    override fun message() = "No internet connection"
}

class ServiceUnavailableError(private val message: String): Failure {
    override fun message() = message
}

class TimeOutError: Failure {
    override fun message() = "Timeout"
}

class UnknownError(private val message: String): Failure {
    override fun message() = message
}