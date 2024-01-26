package com.maxim.diaryforstudents.diary.eduData

import com.maxim.diaryforstudents.performance.eduData.EduCloudMark

data class EduDiaryResponse(
    val success: Boolean,
    val message: String,
    val data: List<EduDiaryLesson>
)

data class EduDiaryLesson(
    val SUBJECT_NAME: String,
    val TEACHER_NAME: String,
    val LESSON_TIME_BEGIN: String,
    val LESSON_TIME_END: String,
    val TOPIC: String?,
    val HOMEWORK: String?,
    val MARKS: List<EduCloudMark>?
)
