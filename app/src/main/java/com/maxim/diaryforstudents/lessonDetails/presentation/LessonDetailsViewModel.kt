package com.maxim.diaryforstudents.lessonDetails.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.presentation.SimpleInit
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.openNews.Share
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LessonDetailsViewModel @Inject constructor(
    private val communication: LessonDetailsCommunication,
    private val storage: LessonDetailsStorage.Read,
    private val navigation: Navigation.Update,
) : ViewModel(), GoBack, SaveAndRestore, Reload, SimpleInit, Communication.Observe<LessonDetailsState> {

    override fun init() {
        if (storage.isEmpty()) {
            communication.update(LessonDetailsState.Loading)
            storage.setCallback(this)
        } else
            reload()
    }

    fun share(share: Share, type: ShareType) {
        type.share(share, storage.lesson())
    }

    override fun reload() {
        communication.update(LessonDetailsState.Base(storage.lesson()))
    }

    fun clear() {
        storage.clear()
    }

    override fun goBack() {
        storage.clear()
        navigation.update(Screen.Pop)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        storage.save(bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        storage.restore(bundleWrapper)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<LessonDetailsState>) {
        communication.observe(owner, observer)
    }
}

interface ShareType {
    fun share(share: Share, lesson: DiaryUi.Lesson)

    object Previous: ShareType {
        override fun share(share: Share, lesson: DiaryUi.Lesson) {
            lesson.sharePreviousHomework(share)
        }
    }

    object Actual: ShareType {
        override fun share(share: Share, lesson: DiaryUi.Lesson) {
            lesson.shareActualHomework(share)
        }
    }

    object All: ShareType {
        override fun share(share: Share, lesson: DiaryUi.Lesson) {
            lesson.shareAllHomework(share)
        }
    }
}