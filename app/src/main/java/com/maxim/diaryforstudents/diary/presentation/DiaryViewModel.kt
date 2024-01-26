package com.maxim.diaryforstudents.diary.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.diary.eduData.EduDiaryRepository

class DiaryViewModel(
    private val repository: EduDiaryRepository,
    private val communication: DiaryCommunication,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<DiaryState> {
    private var actualDay = 0

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            actualDay = repository.actualDate()
            communication.update(DiaryState.Progress)
            reload()
        }
    }

    fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(COMMUNICATION_RESTORE_KEY, bundleWrapper)
        bundleWrapper.save(ACTUAL_DAY_RESTORE_KEY, actualDay)
    }

    fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(COMMUNICATION_RESTORE_KEY, bundleWrapper)
        setActualDay(bundleWrapper.restore(ACTUAL_DAY_RESTORE_KEY)!!)
    }

    fun nextDay() {
        actualDay++
        reload()
    }

    fun previousDay() {
        actualDay--
        reload()
    }

    fun setActualDay(day: Int) {
        actualDay = day
        reload()
    }

    fun homeworkToShare(): String {
        return repository.homeworks(actualDay)
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(DiaryViewModel::class.java)
    }

    fun reload() {
        handle({repository.day(actualDay)}) { day ->
            communication.update(DiaryState.Base(day.toUi(), repository.dayList(actualDay).map { it.toUi() }))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<DiaryState>) {
        communication.observe(owner, observer)
    }

    companion object {
        private const val COMMUNICATION_RESTORE_KEY = "diary_communication_restore"
        private const val ACTUAL_DAY_RESTORE_KEY = "diary_actual_day_restore"
    }
}