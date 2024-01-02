package com.maxim.diaryforstudents.editDiary.edit.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.editDiary.common.CreateLessonCache
import com.maxim.diaryforstudents.editDiary.common.SelectedClassCache
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CreateLessonScreen
import com.maxim.diaryforstudents.editDiary.edit.data.EditDiaryRepository
import java.io.Serializable

class EditDiaryViewModel(
    private val repository: EditDiaryRepository,
    private val communication: EditDiaryCommunication,
    private val selectedClassCache: SelectedClassCache.Read,
    private val createCache: CreateLessonCache.Update,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<EditDiaryState>, ReloadAfterDismiss {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(EditDiaryState.Loading)
            createCache.cacheAfterDismiss(this)
            handle({ repository.init(selectedClassCache.read()) }) {
                communication.update(EditDiaryState.Base(it.map { data -> data.mapToUi() }))
            }
        }
    }

    fun save(bundleWrapper: BundleWrapper.Save) {
        selectedClassCache.save(bundleWrapper)
        createCache.save(bundleWrapper)
        communication.save(RESTORE_KEY, bundleWrapper)
        repository.save(bundleWrapper)
    }

    fun restore(bundleWrapper: BundleWrapper.Restore) {
        selectedClassCache.restore(bundleWrapper)
        createCache.restore(bundleWrapper)
        communication.restore(RESTORE_KEY, bundleWrapper)
        repository.restore(bundleWrapper)
        createCache.cacheAfterDismiss(this)
    }

    fun newLesson() {
        createCache.clearLesson()
        navigation.update(CreateLessonScreen)
    }

    fun editLesson(
        date: Int,
        startTime: String,
        endTime: String,
        theme: String,
        homework: String
    ) {
        createCache.cacheLesson(date, startTime, endTime, theme, homework)
        navigation.update(CreateLessonScreen)
    }

    fun setGrade(grade: Int?, userId: String, date: Int) {
        handle({ repository.setGrade(grade, userId, date) }) {}
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

    companion object {
        private const val RESTORE_KEY = "edit_diary_communication_restore"
    }
}

interface ReloadAfterDismiss: Serializable {
    fun reload()
}