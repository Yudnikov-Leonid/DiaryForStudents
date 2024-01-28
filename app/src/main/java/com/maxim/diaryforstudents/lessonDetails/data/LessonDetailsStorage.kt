package com.maxim.diaryforstudents.lessonDetails.data

import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.diary.presentation.DiaryUi

interface LessonDetailsStorage {
    interface Save {
        fun save(value: DiaryUi.Lesson)
    }

    interface Read: SaveAndRestore {
        fun lesson(): DiaryUi.Lesson
    }

    interface Mutable : Save, Read

    class Base : Mutable {
        private lateinit var cache: DiaryUi.Lesson

        override fun save(value: DiaryUi.Lesson) {
            cache = value
        }

        override fun save(bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(RESTORE_KEY, cache)
        }

        override fun lesson() = cache
        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            cache = bundleWrapper.restore(RESTORE_KEY)!!
        }

        companion object {
            private const val RESTORE_KEY = "lesson_details_storage_restore"
        }
    }
}