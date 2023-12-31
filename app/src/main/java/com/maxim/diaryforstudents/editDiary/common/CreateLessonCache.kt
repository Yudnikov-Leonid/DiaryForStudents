package com.maxim.diaryforstudents.editDiary.common

import com.maxim.diaryforstudents.editDiary.edit.presentation.ReloadAfterDismiss

interface CreateLessonCache {
    interface Update {
        fun cacheName(value: String)
        fun cacheClassId(value: String)
        fun cacheAfterDismiss(value: ReloadAfterDismiss)
    }

    interface Read {
        fun name(): String
        fun classId(): String
        fun afterDismiss(): ReloadAfterDismiss
    }

    interface Mutable : Update, Read

    class Base : Mutable {
        private var name: String = ""
        private var classId: String = ""
        private var reloadAfterDismiss: ReloadAfterDismiss? = null
        override fun cacheName(value: String) {
            name = value
        }

        override fun cacheClassId(value: String) {
            classId = value
        }

        override fun cacheAfterDismiss(value: ReloadAfterDismiss) {
            reloadAfterDismiss = value
        }

        override fun name() = name

        override fun classId() = classId
        override fun afterDismiss() = reloadAfterDismiss!!
    }
}