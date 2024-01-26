package com.maxim.diaryforstudents.performance.eduData

data class EduPerformanceBody(
    private val apikey: String,
    private val guid: String,
    private val from: String,
    private val to: String,
    private val pdskey: String
)