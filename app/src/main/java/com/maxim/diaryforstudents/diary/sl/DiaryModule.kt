package com.maxim.diaryforstudents.diary.sl

import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.eduData.EduDiaryRepository
import com.maxim.diaryforstudents.diary.eduData.EduDiaryService
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel

class DiaryModule(private val core: Core, private val clear: ClearViewModel) :
    Module<DiaryViewModel> {
    override fun viewModel() = DiaryViewModel(
        EduDiaryRepository.Base(
            core.retrofit().create(EduDiaryService::class.java),
            Formatter.Base,
            core.eduUser()
        ),
        DiaryCommunication.Base(),
        core.navigation(),
        clear
    )
}