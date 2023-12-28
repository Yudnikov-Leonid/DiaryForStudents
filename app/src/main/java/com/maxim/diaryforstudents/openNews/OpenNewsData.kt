package com.maxim.diaryforstudents.openNews

import com.maxim.diaryforstudents.news.presentation.NewsUi

interface OpenNewsData {
    interface Save {
        fun save(value: NewsUi)
    }

    interface Read {
        fun read(): NewsUi
    }

    interface Mutable : Save, Read
    class Base : Mutable {
        private lateinit var value: NewsUi
        override fun save(value: NewsUi) {
            this.value = value
        }

        override fun read() = value
    }
}