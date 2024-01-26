package com.maxim.diaryforstudents.menu.presentation

import android.view.View
import java.io.Serializable

interface MenuState: Serializable {
    fun show(
        diary: View,
        performance: View,
        profile: View,
        news: View,
    )

    object Student : MenuState {
        override fun show(diary: View, performance: View, profile: View, news: View) {
            diary.visibility = View.VISIBLE
            performance.visibility = View.VISIBLE
            profile.visibility = View.VISIBLE
            news.visibility = View.VISIBLE
        }
    }
}