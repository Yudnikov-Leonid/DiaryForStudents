package com.maxim.diaryforstudents.lessonDetails.presentation

import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.diary.presentation.DiaryUi

interface LessonDetailsState {
    fun show(
        nameTextView: TextView,
        numberTextView: TextView,
        teacherTextView: TextView,
        topicTextView: TextView,
        topicTitle: TextView,
        homeworkTextView: TextView,
        homeworkTitle: TextView,
        previousHomeworkTextView: TextView,
        previousHomeworkTitle: TextView,
        marksLayout: LinearLayout,
        markTypeTextView: TextView,
        notesTextView: TextView,
        notesTitle: TextView,
        progressBar: ProgressBar
    )

    object Loading: LessonDetailsState {
        override fun show(
            nameTextView: TextView,
            numberTextView: TextView,
            teacherTextView: TextView,
            topicTextView: TextView,
            topicTitle: TextView,
            homeworkTextView: TextView,
            homeworkTitle: TextView,
            previousHomeworkTextView: TextView,
            previousHomeworkTitle: TextView,
            marksLayout: LinearLayout,
            markTypeTextView: TextView,
            notesTextView: TextView,
            notesTitle: TextView,
            progressBar: ProgressBar
        ) {
            progressBar.visibility = View.VISIBLE
        }
    }

    class Base(
        private val lesson: DiaryUi.Lesson, private val colorManager: ColorManager
    ): LessonDetailsState {
        override fun show(
            nameTextView: TextView,
            numberTextView: TextView,
            teacherTextView: TextView,
            topicTextView: TextView,
            topicTitle: TextView,
            homeworkTextView: TextView,
            homeworkTitle: TextView,
            previousHomeworkTextView: TextView,
            previousHomeworkTitle: TextView,
            marksLayout: LinearLayout,
            markTypeTextView: TextView,
            notesTextView: TextView,
            notesTitle: TextView,
            progressBar: ProgressBar
        ) {
            lesson.showName(nameTextView)
            lesson.showNumber(numberTextView)
            lesson.showTeacherName(teacherTextView)
            lesson.showTopic(topicTextView, topicTitle)
            lesson.showHomework(homeworkTextView, homeworkTitle)
            lesson.showPreviousHomework(previousHomeworkTextView, previousHomeworkTitle)
            lesson.showMarks(marksLayout, colorManager)
            lesson.showMarkType(markTypeTextView)
            lesson.showNotes(notesTextView, notesTitle)
            progressBar.visibility = View.GONE
        }
    }
}