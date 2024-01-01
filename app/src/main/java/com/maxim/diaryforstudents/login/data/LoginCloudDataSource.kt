package com.maxim.diaryforstudents.login.data

import com.maxim.diaryforstudents.core.service.MyUser
import com.maxim.diaryforstudents.core.service.Service

interface LoginCloudDataSource {
    suspend fun login()

    class Base(
        private val service: Service,
        private val myUser: MyUser
    ) : LoginCloudDataSource {
        override suspend fun login() {
            val uId = myUser.id()
            val email = myUser.email()
            val name = myUser.name()
            service.setValue("users", uId, "email", email)
            service.setValue("users", uId, "name", name)
        }
    }
}