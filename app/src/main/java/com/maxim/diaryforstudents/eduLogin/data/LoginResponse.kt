package com.maxim.diaryforstudents.eduLogin.data

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData
)

data class LoginData(
    val LOGIN: String,
    val SCHOOLS: List<LoginSchools>
)

data class LoginSchools(
    val PARTICIPANT: LoginParticipant
)

data class LoginParticipant(
    val SYS_GUID: String,
    val SURNAME: String,
    val NAME: String,
    val SECONDNAME: String,
    val GRADE: LoginGrade
)

data class LoginGrade(
    val NAME: String,
    val SCHOOL: LoginSchool
)

data class LoginSchool(
    val NAME: String
)