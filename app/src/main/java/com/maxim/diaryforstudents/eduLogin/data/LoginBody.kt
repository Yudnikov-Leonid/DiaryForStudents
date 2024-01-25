package com.maxim.diaryforstudents.eduLogin.data

data class LoginBody(
    private val api_key: String,
    private val login: String,
    private val password: String
)
