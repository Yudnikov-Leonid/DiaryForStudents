package com.maxim.diaryforstudents.diary.data

import retrofit2.http.Body
import retrofit2.http.POST

interface DiaryService {
    @POST("diaryday")
    suspend fun getDay(@Body body: DiaryBody): DiaryResponse
}