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
            bundleWrapper.save(CACHE_RESTORE_KEY, cache)
            bundleWrapper.save(QUARTER_RESTORE_KEY, cachedQuarter)
        }

        override fun read() = Pair(cache, cachedQuarter)
        override fun clear() {
            cache = ""
            cachedQuarter = -1
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            cache = bundleWrapper.restore(CACHE_RESTORE_KEY) ?: ""
            cachedQuarter = bundleWrapper.restore(QUARTER_RESTORE_KEY) ?: -1
        }

        companion object {
            private const val CACHE_RESTORE_KEY = "analytics_storage_restore"
            private const val QUARTER_RESTORE_KEY = "analytics_storage_restore"
        }
    }
}