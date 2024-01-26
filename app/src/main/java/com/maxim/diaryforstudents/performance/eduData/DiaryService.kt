package com.maxim.diaryforstudents.performance.eduData

import retrofit2.http.Body
import retrofit2.http.POST

interface DiaryService {
    @POST("marksbyperiod")
    suspend fun getMarks(@Body body: EduPerformanceBody): EduPerformanceResponse

    @POST("periodmarks")
    suspend fun getFinalMarks(@Body body: EduPerformanceFinalBody): EduPerformanceFinalResponse
}