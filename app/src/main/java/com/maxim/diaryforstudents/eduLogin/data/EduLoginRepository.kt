package com.maxim.diaryforstudents.eduLogin.data

interface EduLoginRepository {
    suspend fun login(login: String, password: String): EduLoginResult

    class Base: EduLoginRepository {
        override suspend fun login(login: String, password: String): EduLoginResult {
            Thread.sleep(500)
            return EduLoginResult.Failure("something went wrong")
        }
    }
}