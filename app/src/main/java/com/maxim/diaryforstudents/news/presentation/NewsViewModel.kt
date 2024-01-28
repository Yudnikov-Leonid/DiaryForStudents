package com.maxim.diaryforstudents.news.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.news.data.NewsData
import com.maxim.diaryforstudents.news.data.NewsRepository
import com.maxim.diaryforstudents.openNews.OpenNewsStorage
import com.maxim.diaryforstudents.openNews.OpenNewsScreen

class NewsViewModel(
    private val repository: NewsRepository,
    private val communication: NewsCommunication,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    private val openNewsStorage: OpenNewsStorage.Save,
    private val mapper: NewsData.Mapper<NewsUi>
) : BaseViewModel(), Reload, Communication.Observe<NewsState>, Init, GoBack {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(NewsState.Loading)
        }
        repository.init(this)
    }

    fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(RESTORE_KEY, bundleWrapper)
    }

    fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(RESTORE_KEY, bundleWrapper)
    }

    override fun reload() {
        communication.update(NewsState.Base(repository.data().map { it.map(mapper) }))
    }

    override fun error(message: String) {
        communication.update(NewsState.Base(listOf(NewsUi.Failure(message))))
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(NewsViewModel::class.java)
    }

    fun open(value: NewsUi) {
        openNewsStorage.save(value)
        navigation.update(OpenNewsScreen)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<NewsState>) {
        communication.observe(owner, observer)
    }

    companion object {
        private const val RESTORE_KEY = "news_restore"
    }
}