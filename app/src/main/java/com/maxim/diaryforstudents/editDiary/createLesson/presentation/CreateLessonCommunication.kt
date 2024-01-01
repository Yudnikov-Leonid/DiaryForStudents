package com.maxim.diaryforstudents.editDiary.createLesson.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface CreateLessonCommunication: Communication.Mutable<CreateLessonState> {
    class Base: Communication.Regular<CreateLessonState>(), CreateLessonCommunication
}