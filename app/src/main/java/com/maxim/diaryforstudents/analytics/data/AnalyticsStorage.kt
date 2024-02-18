package com.maxim.diaryforstudents.analytics.data

import com.maxim.diaryforstudents.core.presentation.BundleWrapper

interface AnalyticsStorage {
    interface Save {
        fun save(lessonName: String)
    }

    interface Read {
        fun read(): String
        fun clear()
        fun save(bundleWrapper: BundleWrapper.Save)
        fun restore(bundleWrapper: BundleWrapper.Restore)
    }

    interface Mutable: Save, Read

    class Base: Mutable {
        private var cache = ""

        override fun save(lessonName: String) {
            cache = lessonName
        }

        override fun save(bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(RESTORE_KEY, cache)
        }

        override fun read() = cache
        override fun clear() {
            cache = ""
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            cache = bundleWrapper.restore(RESTORE_KEY) ?: ""
        }

        companion object {
            private const val RESTORE_KEY = "analytics_storage_restore"
        }
    }
}