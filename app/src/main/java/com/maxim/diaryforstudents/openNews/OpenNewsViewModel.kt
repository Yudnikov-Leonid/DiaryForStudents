package com.maxim.diaryforstudents.openNews

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.news.presentation.NewsUi

class OpenNewsViewModel(
    private val data: OpenNewsStorage.Read,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel
) : BaseViewModel(), Init, GoBack, SaveAndRestore, Communication.Observe<NewsUi> {
    private val mutableLiveData = MutableLiveData<NewsUi>()
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            mutableLiveData.value = data.read()
    }

    fun share(share: Share) {
        data.read().share(share)
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(OpenNewsViewModel::class.java)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        data.save(bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        data.restore(bundleWrapper)
        mutableLiveData.value = data.read()
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<NewsUi>) {
        mutableLiveData.observe(owner, observer)
    }
}