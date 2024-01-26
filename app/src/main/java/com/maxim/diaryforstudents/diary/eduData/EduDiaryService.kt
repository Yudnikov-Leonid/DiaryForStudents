package com.maxim.diaryforstudents.diary.eduData

import retrofit2.http.Body
import retrofit2.http.POST

interface EduDiaryService {
    @POST("diaryday")
    suspend fun getDay(@Body body: EduDiaryBody): EduDiaryResponse
}