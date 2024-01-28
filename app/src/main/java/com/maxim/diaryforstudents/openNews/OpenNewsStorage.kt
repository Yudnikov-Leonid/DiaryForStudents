package com.maxim.diaryforstudents.openNews

import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.news.presentation.NewsUi

interface OpenNewsStorage {
    interface Save {
        fun save(value: NewsUi)
    }

    interface Read {
        fun read(): NewsUi
        fun save(bundleWrapper: BundleWrapper.Save)
        fun restore(bundleWrapper: BundleWrapper.Restore)
    }

    interface Mutable : Save, Read
    class Base : Mutable {
        private lateinit var value: NewsUi
        override fun save(value: NewsUi) {
            this.value = value
        }

        override fun save(bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(RESTORE_KEY, value)
        }

        override fun read() = value
        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            value = bundleWrapper.restore(RESTORE_KEY)!!
        }

        companion object {
            private const val RESTORE_KEY = "open_news_restore"
        }
    }
}