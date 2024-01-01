package com.maxim.diaryforstudents.login.data

import com.google.android.gms.common.api.ApiException
import com.maxim.diaryforstudents.core.service.MyUser

interface LoginRepository {
    fun userNotLoggedIn(): Boolean
    suspend fun handleResult(authResult: AuthResultWrapper): LoginResult
    class Base(
        private val cloudDataSource: LoginCloudDataSource,
        private val myUser: MyUser
    ) : LoginRepository {
        override fun userNotLoggedIn() = myUser.userNotLoggedIn()

        override suspend fun handleResult(authResult: AuthResultWrapper): LoginResult {
            if (authResult.isSuccessful()) {
                try {
                    val task = authResult.task()
                    val account = task.getResult(ApiException::class.java)
                    val idToken = account.idToken

                    return if (idToken == null) {
                        LoginResult.Failed("something went wrong")
                    } else {
                        val (successful, errorMessage) = myUser.signIn(idToken)
                        if (successful) {
                            cloudDataSource.login()
                            LoginResult.Successful
                        } else {
                            LoginResult.Failed(errorMessage)
                        }
                    }
                } catch (e: Exception) {
                    return LoginResult.Failed(e.message ?: "something went wrong")
                }
            } else {
                return LoginResult.Failed("not successful to login")
            }
        }
    }
}