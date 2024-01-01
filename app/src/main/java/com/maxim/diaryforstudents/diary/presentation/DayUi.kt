package com.maxim.diaryforstudents.diary.presentation

import android.content.res.Configuration
import android.view.View
import android.widget.TextView
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.Formatter

data class DayUi(
    private val date: Int,
    private val isSelected: Boolean
) {
    fun same(item: DayUi) = item.date == date
    fun showDayOfTheWeek(textView: TextView) {
        textView.text = Formatter.Base.format("EE", date)
    }

    fun setSelectedColor(view: View) {
        val isNight =
            view.context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        val color =
            view.context.getColor(
                if (isSelected && isNight) R.color.night_selected_button
                else if (isSelected) R.color.selected_button
                else if (isNight) R.color.light_night_background
                else R.color.white)
        view.setBackgroundColor(color)
    }

    fun showDate(textView: TextView) {
        textView.text = Formatter.Base.format("dd", date)
    }

    fun selectDay(listener: DiaryDaysAdapter.Listener) {
        listener.selectDay(date)
    }
}