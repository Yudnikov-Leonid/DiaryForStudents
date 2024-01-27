package com.maxim.diaryforstudents.performance.data

data class PerformanceBody(
    private val apikey: String,
    private val guid: String,
    private val from: String,
    private val to: String,
    private val pdskey: String
)