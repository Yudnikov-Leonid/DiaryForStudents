package com.maxim.diaryforstudents.editDiary.common

import com.maxim.diaryforstudents.core.presentation.Communication

interface SelectedClassCache {
    interface Update : Communication.Update<String>
    interface Read {
        fun read(): String
    }

    interface Mutable : Update, Read
    class Base : Mutable {
        private var cache: String = ""
        override fun update(value: String) {
            this.cache = value
        }

        override fun read() = cache
    }
}