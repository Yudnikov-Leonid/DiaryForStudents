package com.maxim.diaryforstudents.diary.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.diary.data.DiaryRepository

class DiaryViewModel(
    private val repository: DiaryRepository,
    private val communication: DiaryCommunication,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Reload, Communication.Observe<DiaryState> {
    private var actualDay = 0
    private var week = 0
    fun init() {
        if (actualDay == 0) {
            communication.update(DiaryState.Progress)
            actualDay = repository.actualDate()
        }
        week = (actualDay + 3) / 7 - 1
        handle({
            repository.init(this, week)
        }) {}
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
        if (week != (actualDay + 3) / 7 - 1) init()
        else reload()
    }

    fun previousDay() {
        actualDay--
        if (week != (actualDay + 3) / 7 - 1) init()
        else reload()
    }

    fun setActualDay(day: Int) {
        actualDay = day
        if (week != (actualDay + 3) / 7 - 1) init()
        else reload()
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(DiaryViewModel::class.java)
    }

    override fun reload() {
        communication.update(
            DiaryState.Base(
                repository.date(actualDay).toUi(),
                repository.dayList(actualDay).map { it.toUi() }
            )
        )
    }

    override fun error(message: String) {
        communication.update(DiaryState.Error(message))
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<DiaryState>) {
        communication.observe(owner, observer)
    }

    companion object {
        private const val COMMUNICATION_RESTORE_KEY = "diary_communication_restore"
        private const val ACTUAL_DAY_RESTORE_KEY = "diary_actual_day_restore"
    }
}