package com.maxim.diaryforstudents.lessonDetails.presentation

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage

class LessonDetailsViewModel(
    private val storage: LessonDetailsStorage.Read,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : ViewModel(), GoBack, SaveAndRestore, Reload {

    private val viewList = mutableListOf<TextView>()
    @SuppressLint("StaticFieldLeak")
    private var marksLayout: LinearLayout? = null
    @SuppressLint("StaticFieldLeak")
    private var progressBar: ProgressBar? = null

    fun show(
        nameTextView: TextView,
        teacherTextView: TextView,
        topicTextView: TextView,
        topicTitle: TextView,
        homeworkTextView: TextView,
        homeworkTitle: TextView,
        previousHomeworkTextView: TextView,
        previousHomeworkTitle: TextView,
        marksLayout: LinearLayout,
        notesTitle: TextView,
        notesTextView: TextView,
        progressBar: ProgressBar
    ) {

        viewList.addAll(
            listOf(
                nameTextView,
                teacherTextView,
                topicTextView,
                topicTitle,
                homeworkTextView,
                homeworkTitle,
                previousHomeworkTextView,
                previousHomeworkTitle,
                notesTitle,
                notesTextView
            )
        )
        this.marksLayout = marksLayout
        this.progressBar = progressBar

        progressBar.visibility = if (!storage.isEmpty()) {
            reload()
            View.GONE
        }
        else {
            storage.setCallback(this)
            View.VISIBLE
        }
    }

    override fun reload() {
        val lesson = storage.lesson()
        lesson.showName(viewList[0])
        lesson.showTeacherName(viewList[1])
        lesson.showTopic(viewList[2], viewList[3])
        lesson.showHomework(viewList[4], viewList[5])
        lesson.showPreviousHomework(viewList[6], viewList[7])
        lesson.showMarks(marksLayout!!)
        lesson.showNotes(viewList[8], viewList[9])
        progressBar!!.visibility = View.GONE

        viewList.clear()
        marksLayout = null
        progressBar = null
    }

    fun clear() {
        clearViewModel.clearViewModel(LessonDetailsViewModel::class.java)
        viewList.clear()
        marksLayout = null
        progressBar = null
        storage.clear()
    }

    override fun goBack() {
        storage.clear()
        navigation.update(Screen.Pop)
        clearViewModel.clearViewModel(LessonDetailsViewModel::class.java)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        storage.save(bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        storage.restore(bundleWrapper)
    }
}