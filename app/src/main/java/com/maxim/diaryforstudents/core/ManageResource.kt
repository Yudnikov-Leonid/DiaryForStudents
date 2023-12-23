package com.maxim.diaryforstudents.core

import android.content.res.Resources
import androidx.annotation.StringRes

interface ManageResource {
    fun string(@StringRes key: Int): String
    class Base(private val resources: Resources) : ManageResource {
        override fun string(key: Int) = resources.getString(key)
    }
}