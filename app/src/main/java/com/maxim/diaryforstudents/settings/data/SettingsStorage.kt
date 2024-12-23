package com.maxim.diaryforstudents.settings.data

import com.maxim.diaryforstudents.core.data.SimpleStorage

interface SettingsStorage {
    interface Read {
        fun read(id: Int, defaultValue: Boolean = true): Boolean
    }

    interface Save {
        fun set(id: Int, value: Boolean)
    }

    interface Mutable : Read, Save

    class Base(private val simpleStorage: SimpleStorage): Mutable {
        override fun set(id: Int, value: Boolean) {
            simpleStorage.save(id.toString(), value)
        }

        override fun read(id: Int, defaultValue: Boolean) = simpleStorage.read(id.toString(), defaultValue)
    }
}