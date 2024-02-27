package com.maxim.diaryforstudents.performance.common.domain

import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.sl.ManageResource

interface Failure {
    fun message(manageResource: ManageResource): String
}

class NoInternetConnectionError: Failure {
    override fun message(manageResource: ManageResource): String = manageResource.string(R.string.no_internet_connection)
}

class ServiceUnavailableError(private val message: String): Failure {
    override fun message(manageResource: ManageResource): String = message
}

class TimeOutError: Failure {
    override fun message(manageResource: ManageResource): String = manageResource.string(R.string.timeout)
}

class UnknownError(private val message: String): Failure {
    override fun message(manageResource: ManageResource): String = message
}