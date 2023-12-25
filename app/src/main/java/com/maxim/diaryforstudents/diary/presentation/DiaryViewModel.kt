package com.maxim.diaryforstudents.diary.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.BaseViewModel
import com.maxim.diaryforstudents.core.ClearViewModel
import com.maxim.diaryforstudents.core.Communication
import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.core.RunAsync
import com.maxim.diaryforstudents.core.Screen
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.news.presentation.Reload

//todo make tests
class DiaryViewModel(
    private val repository: DiaryRepository,
    private val communication: DiaryCommunication.Mutable,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Reload, Communication.Observe<DiaryState> {
    private var actualDay = 0
    private var week = 0
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(DiaryState.Progress)
            actualDay = repository.actualDate()
        }
        week = (actualDay + 3) / 7 - 1
        handle({
            repository.init(this, week)
        }) {}
    }

    fun nextDay() {
        actualDay++
        if (week != (actualDay + 3) / 7 - 1) init(false)
        else reload()
    }

    fun previousDay() {
        actualDay--
        if (week != (actualDay + 3) / 7 - 1) init(false)
        else reload()
    }

    fun setActualDay(day: Int) {
        actualDay = day
        reload()
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(DiaryViewModel::class.java)
    }

    override fun reload() {
        communication.update(
            DiaryState.Base(
                repository.data(actualDay).toUi(),
                repository.dayList(actualDay)
            )
        )
    }

    override fun error(message: String) {
        communication.update(DiaryState.Error(message))
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<DiaryState>) {
        communication.observe(owner, observer)
    }
}