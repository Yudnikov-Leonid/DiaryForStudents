package com.maxim.diaryforstudents.diary.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.diary.domain.DayDomain
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.domain.DiaryInteractor
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsScreen
import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

class DiaryViewModel(
    private val filters: MutableList<DiaryUi.Mapper<Boolean>>,
    private val interactor: DiaryInteractor,
    private val communication: DiaryCommunication,
    private val storage: LessonDetailsStorage.Save,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    private val mapper: DiaryDomain.Mapper<DiaryUi>,
    private val dayMapper: DayDomain.Mapper<DayUi>,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<DiaryState>, Init, GoBack, SaveAndRestore {
    private var actualDay = 0
    private var nameFilter = ""

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            actualDay = interactor.actualDate()
            reload(true)
        }
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(COMMUNICATION_RESTORE_KEY, bundleWrapper)
        bundleWrapper.save(ACTUAL_DAY_RESTORE_KEY, actualDay)
        bundleWrapper.save(NAME_FILTER_RESTORE_KEY, nameFilter)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(COMMUNICATION_RESTORE_KEY, bundleWrapper)
        nameFilter = bundleWrapper.restore(NAME_FILTER_RESTORE_KEY) ?: ""
        actualDay = bundleWrapper.restore(ACTUAL_DAY_RESTORE_KEY) ?: interactor.actualDate()
    }

    fun nextDay() {
        actualDay++
        reload(false)
    }

    fun previousDay() {
        actualDay--
        reload(false)
    }

    fun setActualDay(day: Int) {
        actualDay = day
        reload(false)
    }

    fun openDetails(item: DiaryUi.Lesson) {
        storage.save(item)
        navigation.update(LessonDetailsScreen)
    }

    fun homeworkToShare() = interactor.homeworks(actualDay)
    fun previousHomeworkToShare() = interactor.previousHomeworks(actualDay)

    fun setFilter(position: Int, isChecked: Boolean) {
        val checks = checks()
        checks[position] = isChecked
        interactor.saveFilters(checks)
        reload(false)
    }

    fun setNameFilter(value: String) {
        filters[3] = object : DiaryUi.Mapper<Boolean> {
            override fun map(
                name: String, topic: String, homework: String,
                startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Mark>
            ) = name.contains(value, true)
        }
        nameFilter = value
        reload(false)
    }

    fun setHomeworkType(from: Boolean) {
        interactor.saveHomeworkFrom(from)
        reload(false)
    }

    fun checks() = interactor.filters()
    fun homeworkFrom() = interactor.homeworkFrom()
    fun nameFilter() = nameFilter

    override fun goBack() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(DiaryViewModel::class.java)
    }

    fun reload(showLoading: Boolean) {
        if (showLoading)
            communication.update(DiaryState.Progress)
        handle({ interactor.day(actualDay) }) { day ->
            if (day is DiaryDomain.Error) {
                communication.update(
                    DiaryState.Error(day.message())
                )
                return@handle
            }

            val checks = checks()
            var filteredDay = day.map(mapper)
            checks.forEachIndexed { i, b ->
                if (b)
                    filteredDay = filteredDay.filter(filters[i])
            }
            if (filters.size >= 4)
                filteredDay = filteredDay.filter(filters[3])
            communication.update(
                DiaryState.Base(
                    filteredDay,
                    interactor.dayList(actualDay).map { it.map(dayMapper) },
                    checks.filter { it }.size + if (nameFilter.isNotEmpty()) 1 else 0,
                    interactor.homeworkFrom()
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
        private const val NAME_FILTER_RESTORE_KEY = "diary_name_filter_restore"
    }
}