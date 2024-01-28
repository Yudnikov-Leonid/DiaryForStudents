package com.maxim.diaryforstudents.diary.presentation

import android.content.res.Configuration
import android.view.View
import android.widget.TextView
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.Formatter
import java.io.Serializable

data class DayUi(
    private val date: Int,
    private val isSelected: Boolean
): Serializable {
    fun same(item: DayUi) = item.date == date
    fun showDayOfTheWeek(textView: TextView) {
        textView.text = Formatter.Base.format("EE", date)
    }

    fun setSelectedColor(view: View) {
        val color =
            view.context.getColor(
                if (isSelected) R.color.selected_button else R.color.light_background)
        view.setBackgroundColor(color)
    }

    fun showDate(textView: TextView) {
        textView.text = Formatter.Base.format("dd", date)
    }

    fun selectDay(listener: DiaryDaysAdapter.Listener) {
        listener.selectDay(date)
    }
}