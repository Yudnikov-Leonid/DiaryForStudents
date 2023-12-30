package com.maxim.diaryforstudents.editDiary.edit.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.editDiary.common.SelectedClassCache
import com.maxim.diaryforstudents.editDiary.edit.data.EditDiaryRepository

class EditDiaryViewModel(
    private val repository: EditDiaryRepository,
    private val communication: EditDiaryCommunication,
    private val selectedClassCache: SelectedClassCache.Read,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Reload, Communication.Observe<EditDiaryState> {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(EditDiaryState.Loading)
            handle({ repository.init(selectedClassCache.read()) }) {
                communication.update(EditDiaryState.Base(it.map { data -> data.mapToUi() }))
            }
        }
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(EditDiaryViewModel::class.java)
    }

    override fun reload() {
        communication.update(EditDiaryState.Base(repository.data().map { it.mapToUi() }))
    }

    override fun error(message: String) = Unit //todo
    override fun observe(owner: LifecycleOwner, observer: Observer<EditDiaryState>) {
        communication.observe(owner, observer)
    }
}