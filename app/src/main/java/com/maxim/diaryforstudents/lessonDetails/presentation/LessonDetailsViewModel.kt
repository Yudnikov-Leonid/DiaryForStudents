package com.maxim.diaryforstudents.lessonDetails.presentation

import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage

class LessonDetailsViewModel(
    private val storage: LessonDetailsStorage.Read,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
): ViewModel() {

    fun show(
        nameTextView: TextView,
        teacherTextView: TextView,
        topicTextView: TextView,
        homeworkTextView: TextView,
        previousHomeworkTextView: TextView
    ) {
        nameTextView.text = storage.name()
        teacherTextView.text = storage.teacherName()
        val topicText = "Topic: ${storage.topic()}"
        topicTextView.text = topicText
        val homeworkText = "Homework: ${storage.homework()}"
        homeworkTextView.text = homeworkText
        val previousHomeworkText = "Previous homework: ${storage.previousHomework()}"
        previousHomeworkTextView.text = previousHomeworkText
    }

    fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clearViewModel(LessonDetailsViewModel::class.java)
    }
}