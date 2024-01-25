package com.maxim.diaryforstudents.eduLogin.data

import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login")
    suspend fun login(@Body body: LoginBody): LoginResponse
}