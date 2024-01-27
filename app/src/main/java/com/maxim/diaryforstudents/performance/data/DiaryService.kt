package com.maxim.diaryforstudents.performance.data

import retrofit2.http.Body
import retrofit2.http.POST

interface DiaryService {
    @POST("marksbyperiod")
    suspend fun getMarks(@Body body: PerformanceBody): PerformanceResponse

    @POST("periodmarks")
    suspend fun getFinalMarks(@Body body: PerformanceFinalBody): PerformanceFinalResponse
}