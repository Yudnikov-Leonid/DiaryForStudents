package com.maxim.diaryforstudents.lessonDetails.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsCommunication
import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsViewModel

class LessonDetailsModule(private val core: Core, private val clearViewModel: ClearViewModel): Module<LessonDetailsViewModel> {
    override fun viewModel() = LessonDetailsViewModel(
        LessonDetailsCommunication.Base(),
        core.lessonDetailsStorage(),
        core.navigation(),
        clearViewModel
    )
}