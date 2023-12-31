package com.maxim.diaryforstudents.editDiary.createLesson.presentation

import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.editDiary.common.CreateLessonCache
import com.maxim.diaryforstudents.editDiary.createLesson.data.CreateLessonRepository

class CreateLessonViewModel(
    private val repository: CreateLessonRepository,
    private val communication: CreateLessonCommunication,
    private val cache: CreateLessonCache.Read,
    private val validator: UiValidator,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<CreateLessonState> {
    //todo refactor init
    private var cachedDate = 0
    fun init(
        isFirstRun: Boolean,
        startTime: EditText,
        endTime: EditText,
        theme: EditText,
        homework: EditText
    ) {
        if (isFirstRun) {
            communication.update(CreateLessonState.Initial)
            if (cache.lesson() != null) {
                cache.lesson()!!.toUi().showLesson(startTime, endTime, theme, homework)
                cachedDate = cache.lesson()!!.date
            }
        }
    }

    fun clear() {
        clear.clearViewModel(CreateLessonViewModel::class.java)
    }

    fun reloadList() {
        cache.afterDismiss().reload()
    }

    fun save(startTime: String, endTime: String, theme: String, homework: String) {
        try {
            validator.isValid(startTime)
            try {
                validator.isValid(endTime)
                communication.update(CreateLessonState.Loading)

                handle({
                    if (cache.lesson() == null) repository.create(
                        startTime,
                        endTime,
                        theme,
                        homework,
                        cache.name(),
                        cache.classId()
                    ) else repository.update(
                        cachedDate,
                        startTime,
                        endTime,
                        theme,
                        homework,
                        cache.name(),
                        cache.classId()
                    )
                }) {
                    communication.update(it.toState())
                }
            } catch (e: ValidationException) {
                communication.update(CreateLessonState.EndTimeError(e.message!!))
            }
        } catch (e: ValidationException) {
            communication.update(CreateLessonState.StartTimeError(e.message!!))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<CreateLessonState>) {
        communication.observe(owner, observer)
    }
}