package com.maxim.diaryforstudents.settings.data

import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.presentation.Reload

interface LessonsInMenuSettings {
    interface Read {
        fun isShow(): Boolean
        fun setCallback(reload: Reload)
    }

    interface Save {
        fun set(value: Boolean)
    }

    interface Mutable : Read, Save


    class Base(private val simpleStorage: SimpleStorage) : Mutable {
        private var callback: Reload? = null

        override fun set(value: Boolean) {
            simpleStorage.save(KEY, value)
            callback?.reload()
        }

        override fun isShow() = simpleStorage.read(KEY, true)

        override fun setCallback(reload: Reload) {
            callback = reload
        }

        companion object {
            private const val KEY = "show_lessons_in_menu"
        }
    }
}