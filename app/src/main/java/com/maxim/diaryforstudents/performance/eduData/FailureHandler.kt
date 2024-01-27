package com.maxim.diaryforstudents.performance.eduData

import com.maxim.diaryforstudents.performance.domain.Failure
import com.maxim.diaryforstudents.performance.domain.NoInternetConnectionError
import com.maxim.diaryforstudents.performance.domain.ServiceUnavailableError
import com.maxim.diaryforstudents.performance.domain.ServiceUnavailableException
import com.maxim.diaryforstudents.performance.domain.UnknownError
import java.net.UnknownHostException

interface FailureHandler {
    fun handle(e: Exception): Failure

    class Base: FailureHandler {
        override fun handle(e: Exception): Failure {
            return when(e) {
                is UnknownHostException -> NoInternetConnectionError()
                is ServiceUnavailableException -> ServiceUnavailableError(e.message ?: "")
                else -> UnknownError()
            }
        }
    }
}