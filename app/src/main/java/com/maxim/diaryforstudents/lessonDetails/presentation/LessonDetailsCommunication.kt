package com.maxim.diaryforstudents.lessonDetails.presentation

import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface LessonDetailsCommunication: Communication.Mutable<LessonDetailsState> {
    class Base: Communication.Regular<LessonDetailsState>(MutableStateFlow(LessonDetailsState.Empty)), LessonDetailsCommunication
}