package com.maxim.diaryforstudents.editDiary.edit.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.editDiary.edit.data.EditDiaryCloudDataSource
import com.maxim.diaryforstudents.editDiary.edit.data.EditDiaryRepository
import com.maxim.diaryforstudents.editDiary.edit.presentation.EditDiaryCommunication
import com.maxim.diaryforstudents.editDiary.edit.presentation.EditDiaryViewModel

class EditDiaryModule(private val core: Core, private val clear: ClearViewModel) :
    Module<EditDiaryViewModel> {
    override fun viewModel() = EditDiaryViewModel(
        EditDiaryRepository.Base(
            EditDiaryCloudDataSource.Base(core.dataBase(), core.myUser()),
            core.createLessonCache(),
            core.lessonsMapper()
        ),
        EditDiaryCommunication.Base(),
        core.selectedClassCache(),
        core.createLessonCache(),
        core.navigation(),
        clear
    )
}