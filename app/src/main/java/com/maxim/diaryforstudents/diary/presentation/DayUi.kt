package com.maxim.diaryforstudents.diary.presentation

import android.view.View
import android.widget.TextView
import com.maxim.diaryforstudents.R
import java.text.SimpleDateFormat
import java.util.Calendar

data class DayUi(
    private val date: Int,
    private val isSelected: Boolean
) {
    fun same(item: DayUi) = item.date == date
    fun showDayOfTheWeek(textView: TextView) {
        val formatter = SimpleDateFormat("EE")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date * 86400000L
        textView.text = formatter.format(calendar.time)
    }
    fun setSelectedColor(view: View) {
        val color = view.context.getColor(if (isSelected) R.color.selected_button else R.color.white)
        view.setBackgroundColor(color)
    }
    fun showDate(textView: TextView) {
        val formatter = SimpleDateFormat("dd")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date * 86400000L
        textView.text = formatter.format(calendar.time)
    }

    fun selectDay(listener: DiaryDaysAdapter.Listener) {
        listener.selectDay(date)
    }
}