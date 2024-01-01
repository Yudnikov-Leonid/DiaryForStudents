package com.maxim.diaryforstudents.editDiary.common

import com.maxim.diaryforstudents.editDiary.edit.data.GradeData
import com.maxim.diaryforstudents.editDiary.edit.presentation.ReloadAfterDismiss

interface CreateLessonCache {
    interface Update {
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

    interface Read {
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
    }
}