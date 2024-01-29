package com.maxim.diaryforstudents.actualPerformanceSettings.presentation

import com.maxim.diaryforstudents.core.presentation.Reload

interface SaveActualSettingsCommunication {
    interface Save {
        fun setCallback(reload: Reload)
    }

    interface Write {
        fun reload()
    }

    interface Mutable: Save, Write

    class Base: Mutable {
        private var callback: Reload? = null

        override fun setCallback(reload: Reload) {
            callback = reload
        }

        override fun reload() {
            callback?.reload()
        }
    }
}