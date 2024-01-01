package com.maxim.diaryforstudents.editDiary.selectClass.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.editDiary.common.SelectedClassCache
import com.maxim.diaryforstudents.editDiary.edit.presentation.EditDiaryScreen
import com.maxim.diaryforstudents.editDiary.selectClass.data.SelectClassRepository

class SelectClassViewModel(
    private val repository: SelectClassRepository,
    private val communication: SelectClassCommunication,
    private val selectedClassCache: SelectedClassCache.Update,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel
) : BaseViewModel(), Reload, Communication.Observe<SelectClassState> {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(SelectClassState.Loading)
            repository.init(this)
        }
    }

    fun open(id: String) {
        selectedClassCache.update(id)
        navigation.update(EditDiaryScreen)
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(SelectClassViewModel::class.java)
    }

    override fun reload() {
        communication.update(SelectClassState.Base(repository.data().map { it.mapToUi() }))
    }

    override fun error(message: String) = Unit
    override fun observe(owner: LifecycleOwner, observer: Observer<SelectClassState>) {
        communication.observe(owner, observer)
    }
}