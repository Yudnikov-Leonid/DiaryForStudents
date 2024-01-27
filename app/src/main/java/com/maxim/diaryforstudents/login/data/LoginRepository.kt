package com.maxim.diaryforstudents.login.data

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.service.EduUser

interface LoginRepository {
    suspend fun login(login: String, password: String): LoginResult

    class Base(private val service: LoginService, private val eduUser: EduUser) :
        LoginRepository {
        override suspend fun login(login: String, password: String): LoginResult {
            return try {
                val data = service.login(LoginBody(BuildConfig.API_KEY, login, password))
                if (data.success) {
                    val user = data.data.SCHOOLS.first()
                    val guid = user.PARTICIPANT.SYS_GUID
                    val fullName =
                        "${user.PARTICIPANT.SURNAME} ${user.PARTICIPANT.NAME} ${user.PARTICIPANT.SECONDNAME}"
                    val grade = user.PARTICIPANT.GRADE.NAME
                    val school = user.PARTICIPANT.GRADE.SCHOOL.SHORT_NAME
                    eduUser.login(guid, fullName, school, grade)
                    LoginResult.Success
                } else {
                    LoginResult.Failure(data.message)
                }
            } catch (e: Exception) {
                LoginResult.Failure(e.message!!)
            }
        }
    }
}