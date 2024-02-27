package com.maxim.diaryforstudents.lessonDetails.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface LessonDetailsCommunication: Communication.Mutable<LessonDetailsState> {
    class Base: Communication.Regular<LessonDetailsState>(), LessonDetailsCommunication
}