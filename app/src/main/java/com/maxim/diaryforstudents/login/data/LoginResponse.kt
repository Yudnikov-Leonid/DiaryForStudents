package com.maxim.diaryforstudents.login.data

import java.io.Serializable

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData
)

data class LoginData(
    val EMAIL: String,
    val SCHOOLS: List<LoginSchools>
)

data class LoginSchools(
    val PARTICIPANT: LoginParticipant
): Serializable

data class LoginParticipant(
    val SYS_GUID: String,
    val SURNAME: String,
    val NAME: String,
    val SECONDNAME: String,
    val GRADE: LoginGrade
): Serializable

data class LoginGrade(
    val NAME: String,
    val SCHOOL: LoginSchool,
    val GRADE_HEAD: LoginGradeHead
): Serializable

data class LoginSchool(
    val SHORT_NAME: String
): Serializable

data class LoginGradeHead(
    val SURNAME: String,
    val NAME: String,
    val SECONDNAME: String,
): Serializable