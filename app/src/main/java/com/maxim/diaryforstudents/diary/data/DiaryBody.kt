package com.maxim.diaryforstudents.diary.data

data class DiaryBody(
    private val date: String,
    private val apikey: String,
    private val guid: String,
    private val pdakey: String
)