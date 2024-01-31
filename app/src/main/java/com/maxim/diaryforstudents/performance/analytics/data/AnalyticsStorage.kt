package com.maxim.diaryforstudents.performance.analytics.data

interface AnalyticsStorage {
    interface Save {
        fun save(lessonName: String)
    }

    interface Read {
        fun read(): String
        fun clear()
    }

    interface Mutable: Save, Read

    class Base: Mutable {
        private var cache = ""

        override fun save(lessonName: String) {
            cache = lessonName
        }

        override fun read() = cache
        override fun clear() {
            cache = ""
        }
    }
}