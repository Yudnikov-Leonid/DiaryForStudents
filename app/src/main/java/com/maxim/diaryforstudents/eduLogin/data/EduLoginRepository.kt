package com.maxim.diaryforstudents.eduLogin.data

import android.util.Log

interface EduLoginRepository {
    suspend fun login(login: String, password: String): EduLoginResult

    class Base(private val service: LoginService) : EduLoginRepository {
        override suspend fun login(login: String, password: String): EduLoginResult {
            return try {
                val data = service.login(LoginBody("", login, password))
                if (data.success) {
                    Log.d("MyLog", "name: ${data.data.SCHOOLS.first().PARTICIPANT.NAME}")
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