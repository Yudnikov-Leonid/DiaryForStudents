package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.performance.data.CloudMark

data class DiaryResponse(
    val success: Boolean,
    val message: String,
    val data: List<DiaryLesson>
)

data class DiaryLesson(
    val SUBJECT_NAME: String,
    val TEACHER_NAME: String,
    val LESSON_TIME_BEGIN: String,
    val LESSON_TIME_END: String,
    val TOPIC: String?,
    val HOMEWORK: String?,
    val HOMEWORK_PREVIOUS: DiaryPreviousHomework?,
    val MARKS: List<CloudMark>?
)

data class DiaryPreviousHomework(
    val DATE: String,
    val HOMEWORK: String
)