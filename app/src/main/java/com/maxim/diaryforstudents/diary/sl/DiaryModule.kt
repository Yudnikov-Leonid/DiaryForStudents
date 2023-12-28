package com.maxim.diaryforstudents.diary.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.data.DiaryCloudDataSource
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel

class DiaryModule(private val core: Core, private val clear: ClearViewModel): Module<DiaryViewModel> {
    override fun viewModel() = DiaryViewModel(
        DiaryRepository.Base(DiaryCloudDataSource.Base(core.dataBase(), core.lessonsMapper())),
        DiaryCommunication.Base(),
        core.navigation(),
        clear
    )
}