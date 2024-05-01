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
import com.maxim.diaryforstudents.openNews.Share
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val interactor: DiaryInteractor,
    private val communication: DiaryCommunication,
    private val storage: LessonDetailsStorage.Save,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    private val mapper: DiaryDomain.Mapper<DiaryUi>,
    private val dayMapper: DayDomain.Mapper<DayUi>,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<DiaryState>, Init, GoBack, SaveAndRestore, Serializable {
    private var actualDay = 0

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            actualDay = interactor.actualDate()
            reload(true)
        }
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(COMMUNICATION_RESTORE_KEY, bundleWrapper)
        bundleWrapper.save(ACTUAL_DAY_RESTORE_KEY, actualDay)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(COMMUNICATION_RESTORE_KEY, bundleWrapper)
        actualDay = bundleWrapper.restore(ACTUAL_DAY_RESTORE_KEY) ?: interactor.actualDate()
    }

    fun setActualDay(day: Int) {
        actualDay = day
        reload(false)
    }

    fun nextWeek() {
        actualDay += 7
        reload(false)
    }

    fun previousWeek() {
        actualDay -= 7
        reload(false)
    }

    fun actualDay() = actualDay

    fun openDetails(item: DiaryUi.Lesson) {
        storage.save(item)
        navigation.update(LessonDetailsScreen)
    }

    fun shareHomework(share: Share) {
        share.share(interactor.homeworks(actualDay))
    }

    fun sharePreviousHomework(share: Share) {
        share.share(interactor.previousHomeworks(actualDay))
    }

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

            val days = interactor.dayLists(actualDay)
            communication.update(
                DiaryState.Base(
                    day.map(mapper),
                    days.first.map { it.map(dayMapper) },
                    days.second.map { it.map(dayMapper) },
                    days.third.map { it.map(dayMapper) },
                    true
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