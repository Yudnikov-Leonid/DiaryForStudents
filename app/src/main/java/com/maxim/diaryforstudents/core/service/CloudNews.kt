package com.maxim.diaryforstudents.core.service

data class CloudNews(
    val title: String = "",
    val content: String = "",
    val date: Long = 0L,
    val photoUrl: String = "",
    val downloadUrl: String = "",
    val fileName: String = "",
    val status: Int = 0
)