package com.maxim.diaryforstudents.editDiary.edit.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.editDiary.common.CreateLessonCache
import com.maxim.diaryforstudents.editDiary.common.SelectedClassCache
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CreateLessonScreen
import com.maxim.diaryforstudents.editDiary.edit.data.EditDiaryRepository

class EditDiaryViewModel(
    private val repository: EditDiaryRepository,
    private val communication: EditDiaryCommunication,
    private val selectedClassCache: SelectedClassCache.Read,
    private val cache: CreateLessonCache.Update,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<EditDiaryState>, ReloadAfterDismiss {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(EditDiaryState.Loading)
            cache.cacheAfterDismiss(this)
            handle({ repository.init(selectedClassCache.read()) }) {
                communication.update(EditDiaryState.Base(it.map { data -> data.mapToUi() }))
            }
        }
    }

    fun newLesson() {
        cache.clearLesson()
        navigation.update(CreateLessonScreen)
    }

    fun editLesson(
        date: Int,
        startTime: String,
        endTime: String,
        theme: String,
        homework: String
    ) {
        cache.cacheLesson(date, startTime, endTime, theme, homework)
        navigation.update(CreateLessonScreen)
    }

    fun setGrade(grade: Int?, userId: String, date: Int) {
        repository.setGrade(grade, userId, date)
        communication.setGrade(grade, userId, date)
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(EditDiaryViewModel::class.java)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<EditDiaryState>) {
        communication.observe(owner, observer)
    }

    override fun reload() {
        communication.update(EditDiaryState.Loading)
        handle({ repository.init(selectedClassCache.read()) }) {
            communication.update(EditDiaryState.Base(it.map { data -> data.mapToUi() }))
        }
    }
}

interface ReloadAfterDismiss {
    fun reload()
}