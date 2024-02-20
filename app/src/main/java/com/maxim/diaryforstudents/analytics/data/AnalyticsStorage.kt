package com.maxim.diaryforstudents.analytics.data

import com.maxim.diaryforstudents.core.presentation.BundleWrapper

interface AnalyticsStorage {
    interface Save {
        fun save(lessonName: String, currentQuarter: Int)
    }

    interface Read {
        fun read(): Pair<String, Int>
        fun clear()
        fun save(bundleWrapper: BundleWrapper.Save)
        fun restore(bundleWrapper: BundleWrapper.Restore)
    }

    interface Mutable: Save, Read

    class Base: Mutable {
        private var cache = ""
        private var cachedQuarter = -1

        override fun save(lessonName: String, currentQuarter: Int) {
            cache = lessonName
            cachedQuarter = currentQuarter
        }

        override fun save(bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(RESTORE_KEY, cache)
        }

        override fun read() = Pair(cache, cachedQuarter)
        override fun clear() {
            cache = ""
            cachedQuarter = -1
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            cache = bundleWrapper.restore(RESTORE_KEY) ?: ""
        }

        companion object {
            private const val RESTORE_KEY = "analytics_storage_restore"
        }
    }
}