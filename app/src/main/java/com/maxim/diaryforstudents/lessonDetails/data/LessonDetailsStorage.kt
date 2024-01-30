package com.maxim.diaryforstudents.lessonDetails.data

import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.diary.presentation.DiaryUi

interface LessonDetailsStorage {
    interface Save {
        fun save(value: DiaryUi.Lesson)
    }

    interface Read: SaveAndRestore {
        fun lesson(): DiaryUi.Lesson
        fun isEmpty(): Boolean
        fun setCallback(reload: Reload)
        fun clear()
    }

    interface Mutable : Save, Read

    class Base : Mutable {
        private var cache: DiaryUi.Lesson? = null
        private var callback: Reload? = null

        override fun save(value: DiaryUi.Lesson) {
            cache = value
            if (callback != null) {
                callback!!.reload()
                callback = null
            }
        }

        override fun save(bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(RESTORE_KEY, cache!!)
        }

        override fun lesson() = cache!!

        override fun isEmpty() = cache == null

        override fun setCallback(reload: Reload) {
            callback = reload
        }

        override fun clear() {
            cache = null
            callback = null
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            cache = bundleWrapper.restore(RESTORE_KEY)!!
        }

        companion object {
            private const val RESTORE_KEY = "lesson_details_storage_restore"
        }
    }
}