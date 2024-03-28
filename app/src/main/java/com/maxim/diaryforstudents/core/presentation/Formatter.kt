package com.maxim.diaryforstudents.core.presentation

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

interface Formatter {
    fun format(pattern: String, date: Int): String
    fun day(date: Int): String
    fun hoursAndMinutes(time: Long): String

    object Base : Formatter {
        override fun format(pattern: String, date: Int): String {
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date * 86400000L
            return formatter.format(calendar.time)
        }

        override fun day(date: Int): String {
            val formatter = SimpleDateFormat.getDateInstance()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date * 86400000L
            return formatter.format(calendar.time)
        }

        override fun hoursAndMinutes(time: Long): String {
            val formatter = SimpleDateFormat("HHmm", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            return formatter.format(calendar.time)
        }
    }
}