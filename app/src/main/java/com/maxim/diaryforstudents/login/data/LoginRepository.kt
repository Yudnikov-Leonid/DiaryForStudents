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
                    val email = data.data.EMAIL
                    val fullName =
                        "${user.PARTICIPANT.SURNAME} ${user.PARTICIPANT.NAME} ${user.PARTICIPANT.SECONDNAME}"
                    val grade = user.PARTICIPANT.GRADE.NAME
                    val school = user.PARTICIPANT.GRADE.SCHOOL.SHORT_NAME
                    val gradeHead = user.PARTICIPANT.GRADE.GRADE_HEAD
                    val gradeHeadName = "${gradeHead.SURNAME} ${gradeHead.NAME} ${user.PARTICIPANT.SECONDNAME}"
                    eduUser.login(guid, email, fullName, school, grade, gradeHeadName)
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