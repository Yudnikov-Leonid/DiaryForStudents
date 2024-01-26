package com.maxim.diaryforstudents.eduLogin.data

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.service.EduUser

interface EduLoginRepository {
    suspend fun login(login: String, password: String): EduLoginResult

    class Base(private val service: LoginService, private val eduUser: EduUser) :
        EduLoginRepository {
        override suspend fun login(login: String, password: String): EduLoginResult {
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
                    EduLoginResult.Success
                } else {
                    EduLoginResult.Failure(data.message)
                }
            } catch (e: Exception) {
                EduLoginResult.Failure(e.message!!)
            }
        }
    }
}