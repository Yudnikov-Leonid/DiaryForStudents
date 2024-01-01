package com.maxim.diaryforstudents.core.service

data class CloudNews(
    val title: String = "",
    val content: String = "",
    val date: Int = 0,
    val photoUrl: String = ""
)

data class CloudClass(val name: String = "")
data class CloudUser(
    val classId: String = "",
    val email: String = "",
    val name: String = "",
    val status: String = "",
    val lesson: String = ""
)

data class CloudGrade(
    val date: Int = 0,
    val grade: Int? = null,
    val lesson: String = "",
    val quarter: Int? = null,
    val userId: String = ""
)

data class CloudFinalGrade(
    val date: Int = 0,
    val grade: Int = 0,
    val lesson: String = "",
    val userId: String = ""
)

data class CloudLesson(
    val classId: String = "",
    val date: Int = 0,
    val name: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val theme: String = "",
    val homework: String = "",
    val week: Int = 0
)