package com.maxim.diaryforstudents.eduLogin.data

import com.maxim.diaryforstudents.core.data.SimpleStorage

interface EduLoginRepository {
    suspend fun login(login: String, password: String): EduLoginResult

    class Base(private val service: LoginService, private val storage: SimpleStorage) : EduLoginRepository {
        override suspend fun login(login: String, password: String): EduLoginResult {
            return try {
                val data = service.login(LoginBody("3F7G8I2JHGHJ3rrhskjm094321bqWRRolp3510u9", login, password))
                if (data.success) {
                    val guid = data.data.SCHOOLS.first().PARTICIPANT.SYS_GUID
                    val dataLogin = data.data.LOGIN
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