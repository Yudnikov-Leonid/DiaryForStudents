package com.maxim.diaryforstudents.eduLogin.data

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.data.SimpleStorage

interface EduLoginRepository {
    suspend fun login(login: String, password: String): EduLoginResult

    class Base(private val service: LoginService, private val storage: SimpleStorage) : EduLoginRepository {
        override suspend fun login(login: String, password: String): EduLoginResult {
            return try {
                val data = service.login(LoginBody(BuildConfig.API_KEY, login, password))
                if (data.success) {
                    val guid = data.data.SCHOOLS.first().PARTICIPANT.SYS_GUID
                    storage.save("GUID", guid)
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