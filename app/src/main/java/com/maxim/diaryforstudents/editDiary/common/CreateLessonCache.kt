package com.maxim.diaryforstudents.editDiary.common

import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.editDiary.edit.data.GradeData
import com.maxim.diaryforstudents.editDiary.edit.presentation.ReloadAfterDismiss

interface CreateLessonCache {
    interface DeathHandle {
        fun save(bundleWrapper: BundleWrapper.Save)
        fun restore(bundleWrapper: BundleWrapper.Restore)
    }

    interface Update : DeathHandle {
        fun cacheName(value: String)
        fun cacheClassId(value: String)
        fun cacheAfterDismiss(value: ReloadAfterDismiss)
        fun cacheLesson(
            date: Int,
            startTime: String,
            endTime: String,
            theme: String,
            homework: String
        )

        fun clearLesson()
    }

    interface Read : DeathHandle {
        fun name(): String
        fun classId(): String
        fun afterDismiss(): ReloadAfterDismiss
        fun lesson(): GradeData.Date?
    }

    interface Mutable : Update, Read

    class Base : Mutable {
        private var name: String = ""
        private var classId: String = ""
        private var reloadAfterDismiss: ReloadAfterDismiss? = null
        private var lesson: GradeData.Date? = null
        override fun cacheName(value: String) {
            name = value
        }

        override fun cacheClassId(value: String) {
            classId = value
        }

        override fun cacheAfterDismiss(value: ReloadAfterDismiss) {
            reloadAfterDismiss = value
        }

        override fun cacheLesson(
            date: Int,
            startTime: String,
            endTime: String,
            theme: String,
            homework: String
        ) {
            lesson = GradeData.Date(date, startTime, endTime, theme, homework)
        }

        override fun clearLesson() {
            lesson = null
        }

        override fun name() = name
        override fun classId() = classId
        override fun afterDismiss() = reloadAfterDismiss!!
        override fun lesson() = lesson
        override fun save(bundleWrapper: BundleWrapper.Save) {
            lesson?.let { bundleWrapper.save(LESSON_RESTORE_KEY, it) }
            bundleWrapper.save(NAME_RESTORE_KEY, name)
            bundleWrapper.save(CLASS_ID_RESTORE_KEY, classId)
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            lesson = bundleWrapper.restore(LESSON_RESTORE_KEY)
            name = bundleWrapper.restore(NAME_RESTORE_KEY)!!
            classId = bundleWrapper.restore(CLASS_ID_RESTORE_KEY)!!
        }

        companion object {
            private const val LESSON_RESTORE_KEY = "create_lesson_lesson_restore"
            private const val NAME_RESTORE_KEY = "create_lesson_name_restore"
            private const val CLASS_ID_RESTORE_KEY = "create_lesson_class_id_restore"
        }
    }
}