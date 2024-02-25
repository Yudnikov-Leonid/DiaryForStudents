package com.maxim.diaryforstudents.diary.presentation

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.Formatter
import java.io.Serializable

data class DayUi(
    private val date: Int,
    private val isSelected: Boolean
) : Serializable {
    fun same(item: DayUi) = item.date == date
    fun showDayOfTheWeek(textView: TextView) {
        textView.text = Formatter.Base.format("EE", date)
    }

    fun setSelectedColor(textView: TextView, dayNameTextView: TextView) {
        textView.background =
            if (isSelected) ContextCompat.getDrawable(textView.context, R.drawable.day) else null
        textView.setTextColor(
            ContextCompat.getColor(
                textView.context,
                if (isSelected) R.color.white else R.color.black
            )
        )
        dayNameTextView.setTextColor(
            ContextCompat.getColor(
                textView.context,
                if (isSelected) R.color.selected_button else R.color.dark_gray
            )
        )
    }

    fun setWeekendSelectedColor(textView: TextView, dayNameTextView: TextView) {
        textView.background =
            if (isSelected) ContextCompat.getDrawable(textView.context, R.drawable.day) else null
        textView.setTextColor(
            ContextCompat.getColor(
                textView.context,
                if (isSelected) R.color.white else R.color.red
            )
        )
        dayNameTextView.setTextColor(
            ContextCompat.getColor(
                textView.context,
                if (isSelected) R.color.red else R.color.dark_gray
            )
        )
    }

    fun showDate(textView: TextView) {
        textView.text = Formatter.Base.format("dd", date)
    }

    fun selectDay(listener: DaysAdapter.Listener) {
        listener.selectDay(date)
    }
}