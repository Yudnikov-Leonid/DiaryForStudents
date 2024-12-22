package com.maxim.diaryforstudents.settings.data

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.maxim.diaryforstudents.BuildConfig

interface IconManager {
    fun setIcon(id: Int)

    class Base(private val context: Context) : IconManager {
        override fun setIcon(id: Int) {
            val pm = context.packageManager

            val aliases =
                listOf(".Activity1", ".Activity2", ".Activity3", ".Activity4", ".Activity5")

            aliases.forEach { alias ->
                val state = if (alias == ".Activity$id")
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                else
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED

                pm.setComponentEnabledSetting(
                    ComponentName(context, "${BuildConfig.APPLICATION_ID}$alias"),
                    state,
                    PackageManager.DONT_KILL_APP
                )
            }
        }
    }
}