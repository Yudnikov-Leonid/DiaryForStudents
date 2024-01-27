package com.maxim.diaryforstudents.lessonDetails.presentation

import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage

class LessonDetailsViewModel(
    private val storage: LessonDetailsStorage.Read,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
): ViewModel(), GoBack {

    fun show(
        nameTextView: TextView,
        teacherTextView: TextView,
        topicTextView: TextView,
        topicTitle: TextView,
        homeworkTextView: TextView,
        homeworkTitle: TextView,
        previousHomeworkTextView: TextView,
        previousHomeworkTitle: TextView,
        marksLayout: LinearLayout
    ) {
        val lesson = storage.lesson()
        lesson.showName(nameTextView)
        lesson.showTeacherName(teacherTextView)
        lesson.showTopic(topicTextView, topicTitle)
        lesson.showHomework(homeworkTextView, homeworkTitle)
        lesson.showPreviousHomework(previousHomeworkTextView, previousHomeworkTitle)
        lesson.showMarks(marksLayout)
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clearViewModel(LessonDetailsViewModel::class.java)
    }
}