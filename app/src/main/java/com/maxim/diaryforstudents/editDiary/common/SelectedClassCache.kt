package com.maxim.diaryforstudents.editDiary.common

import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication

interface SelectedClassCache {
    interface Update : Communication.Update<String>
    interface Read {
        fun read(): String
        fun save(bundleWrapper: BundleWrapper.Save)
        fun restore(bundleWrapper: BundleWrapper.Restore)
    }

    interface Mutable : Update, Read
    class Base : Mutable {
        private var cache: String = ""
        override fun update(value: String) {
            this.cache = value
        }

        override fun read() = cache
        override fun save(bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(RESTORE_KEY, cache)
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            cache = bundleWrapper.restore(RESTORE_KEY)!!
        }

        companion object {
            private const val RESTORE_KEY = "selected_class_restore"
        }
    }
}