package com.maxim.diaryforstudents.menu.presentation

import android.view.View
import android.widget.ProgressBar

interface MenuState {
    fun show(
        diary: View,
        performance: View,
        profile: View,
        news: View,
        teacherDiary: View,
        progressBar: ProgressBar
    )

    object Loading: MenuState {
        override fun show(
            diary: View,
            performance: View,
            profile: View,
            news: View,
            teacherDiary: View,
            progressBar: ProgressBar
        ) {
            diary.visibility = View.GONE
            performance.visibility = View.GONE
            profile.visibility = View.GONE
            news.visibility = View.GONE
            teacherDiary.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    object Student : MenuState {
        override fun show(
            diary: View,
            performance: View,
            profile: View,
            news: View,
            teacherDiary: View,
            progressBar: ProgressBar
        ) {
            diary.visibility = View.VISIBLE
            performance.visibility = View.VISIBLE
            profile.visibility = View.VISIBLE
            news.visibility = View.VISIBLE
            teacherDiary.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    object Teacher : MenuState {
        override fun show(
            diary: View,
            performance: View,
            profile: View,
            news: View,
            teacherDiary: View,
            progressBar: ProgressBar
        ) {
            diary.visibility = View.GONE
            performance.visibility = View.GONE
            profile.visibility = View.VISIBLE
            news.visibility = View.VISIBLE
            teacherDiary.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}