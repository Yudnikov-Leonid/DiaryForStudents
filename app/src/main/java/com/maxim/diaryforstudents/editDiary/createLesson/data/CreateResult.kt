package com.maxim.diaryforstudents.editDiary.createLesson.data

import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CreateLessonState

interface CreateResult {
    fun toState(): CreateLessonState
    object Success : CreateResult {
        override fun toState() = CreateLessonState.Success
    }

    data class Failure(private val message: String) : CreateResult {
        override fun toState() = CreateLessonState.Error(message)
    }
}