package com.maxim.diaryforstudents.editDiary.createLesson.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.editDiary.createLesson.data.CreateLessonRepository
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CreateLessonCommunication
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CreateLessonViewModel
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.UiValidator

class CreateLessonModule(private val core: Core, private val clear: ClearViewModel) :
    Module<CreateLessonViewModel> {
    override fun viewModel() = CreateLessonViewModel(
        CreateLessonRepository.Base(core.dataBase()),
        CreateLessonCommunication.Base(),
        core.createLessonCache(),
        UiValidator.Empty(),
        clear,
        core
    )
}