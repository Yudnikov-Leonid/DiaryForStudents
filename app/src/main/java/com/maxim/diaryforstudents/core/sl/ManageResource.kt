package com.maxim.diaryforstudents.core.sl

import android.content.res.Resources
import androidx.annotation.StringRes
import javax.inject.Inject

interface ManageResource {
    fun string(@StringRes key: Int): String
    fun string(@StringRes key: Int, format: String): String
    class Base @Inject constructor(private val resources: Resources) : ManageResource {
        override fun string(key: Int) = resources.getString(key)
        override fun string(key: Int, format: String) = resources.getString(key, format)
    }
}