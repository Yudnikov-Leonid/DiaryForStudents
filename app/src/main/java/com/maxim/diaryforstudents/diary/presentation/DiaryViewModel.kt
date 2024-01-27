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

    fun homeworkToShare() = repository.homeworks(actualDay)

    private val filters = mutableListOf<DiaryUi.Mapper<Boolean>>()
    private val checks = mutableListOf(false, false, false)
    fun setFilter(mapper: DiaryUi.Mapper<Boolean>, position: Int, isChecked: Boolean) {
        if (!isChecked)
            filters.remove(mapper)
        else
            filters.add(mapper)
        checks[position] = isChecked
        reload()
    }

    fun checks() = checks.toBooleanArray()

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(DiaryViewModel::class.java)
    }

    fun reload() {
        handle({ repository.day(actualDay) }) { day ->
            var filteredDay = day.toUi()
            filters.forEach { filter ->
                filteredDay = filteredDay.filter(filter)
            }
            communication.update(
                DiaryState.Base(
                    filteredDay,
                    repository.dayList(actualDay).map { it.toUi() },
                    filters.size
                ),
            )
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