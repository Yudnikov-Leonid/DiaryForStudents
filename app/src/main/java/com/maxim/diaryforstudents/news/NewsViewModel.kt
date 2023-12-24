package com.maxim.diaryforstudents.news

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.BaseViewModel
import com.maxim.diaryforstudents.core.ClearViewModel
import com.maxim.diaryforstudents.core.Communication
import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.core.Screen

class NewsViewModel(
    private val repository: NewsRepository,
    private val communication: NewsCommunication.Mutable,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel
) : BaseViewModel(), Reload, Communication.Observe<NewsState> {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            repository.init(this)
    }

    override fun reload() {
        communication.update(NewsState.Base(repository.data().map { it.toUi() }))
    }

    override fun error(message: String) {
        communication.update(NewsState.Base(listOf(NewsUi.Failure(message))))
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(NewsViewModel::class.java)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<NewsState>) {
        communication.observe(owner, observer)
    }
}

interface Reload {
    fun reload()
    fun error(message: String)
}