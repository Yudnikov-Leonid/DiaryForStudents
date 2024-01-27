package com.maxim.diaryforstudents.diary.sl

import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.eduData.EduDiaryRepository
import com.maxim.diaryforstudents.diary.eduData.EduDiaryService
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

class DiaryModule(private val core: Core, private val clear: ClearViewModel) :
    Module<DiaryViewModel> {

    private val homeworkFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Grade>
        ) = homework.isNotEmpty()
    }
    private val topicFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Grade>
        ) = topic.isNotEmpty()
    }
    private val marksFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Grade>
        ) = marks.isNotEmpty()
    }

    override fun viewModel() = DiaryViewModel(
        listOf(homeworkFilter, topicFilter, marksFilter),
        EduDiaryRepository.Base(
            core.retrofit().create(EduDiaryService::class.java),
            Formatter.Base,
            core.eduUser(),
            core.simpleStorage()
        ),
        DiaryCommunication.Base(),
        core.navigation(),
        clear
    )
}