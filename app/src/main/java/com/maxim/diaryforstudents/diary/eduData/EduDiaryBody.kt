package com.maxim.diaryforstudents.diary.eduData

data class EduDiaryBody(
    private val date: String,
    private val apikey: String,
    private val guid: String,
    private val pdakey: String
)