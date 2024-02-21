package com.maxim.diaryforstudents.login.data

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.selectUser.data.SelectUserData

interface LoginRepository {
    suspend fun login(login: String, password: String): LoginResult
    fun users(): List<SelectUserData>
    fun select(position: Int)

    class Base(private val service: LoginService, private val eduUser: EduUser) :
        LoginRepository {
        private val usersList = mutableListOf<LoginSchools>()
        private var cachedEmail = ""

        override suspend fun login(login: String, password: String): LoginResult {
            return try {
                val data = service.login(LoginBody(BuildConfig.API_KEY, login, password))
                if (data.success) {
                    usersList.clear()
                    usersList.addAll(data.data.SCHOOLS)
                    cachedEmail = data.data.EMAIL
                    LoginResult.Success
                } else {
                    LoginResult.Failure(data.message)
                }
            } catch (e: Exception) {
                LoginResult.Failure(e.message!!)
            }
        }

        override fun users(): List<SelectUserData> {
            return usersList.map {
                SelectUserData.Base(
                    "${it.PARTICIPANT.SURNAME} ${it.PARTICIPANT.NAME} ${it.PARTICIPANT.SECONDNAME}",
                    it.PARTICIPANT.GRADE.SCHOOL.SHORT_NAME
                )
            }
        }

        override fun select(position: Int) {
            val user = usersList[position]
            val guid = user.PARTICIPANT.SYS_GUID
            val fullName =
                "${user.PARTICIPANT.SURNAME} ${user.PARTICIPANT.NAME} ${user.PARTICIPANT.SECONDNAME}"
            val grade = user.PARTICIPANT.GRADE.NAME
            val school = user.PARTICIPANT.GRADE.SCHOOL.SHORT_NAME
            val gradeHead = user.PARTICIPANT.GRADE.GRADE_HEAD
            val gradeHeadName =
                "${gradeHead.SURNAME} ${gradeHead.NAME} ${user.PARTICIPANT.SECONDNAME}"
            eduUser.login(guid, cachedEmail, fullName, school, grade, gradeHeadName)
            usersList.clear()
        }
    }
}