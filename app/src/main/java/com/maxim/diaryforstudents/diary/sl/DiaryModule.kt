package com.maxim.diaryforstudents.diary.sl

import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.domain.DiaryInteractor
import com.maxim.diaryforstudents.diary.eduData.EduDiaryRepository
import com.maxim.diaryforstudents.diary.eduData.EduDiaryService
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.performance.eduData.FailureHandler
import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

class DiaryModule(private val core: Core, private val clear: ClearViewModel) :
    Module<DiaryViewModel> {

    private val homeworkFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Mark>
        ) = homework.isNotEmpty()
    }
    private val topicFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Mark>
        ) = topic.isNotEmpty()
    }
    private val marksFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Mark>
        ) = marks.isNotEmpty()
    }
    private val nameFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Mark>
        ) = true
    }

    override fun viewModel() = DiaryViewModel(
        mutableListOf(homeworkFilter, topicFilter, marksFilter, nameFilter),
        DiaryInteractor.Base(
            EduDiaryRepository.Base(
                core.retrofit().create(EduDiaryService::class.java),
                Formatter.Base,
                core.eduUser(),
                core.simpleStorage()
            ), FailureHandler.Base()
        ),
        DiaryCommunication.Base(),
        core.lessonDetailsStorage(),
        core.navigation(),
        clear
    )
}