package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.performance.common.presentation.MarkType

interface HandleMarkType {
    fun handle(value: String): MarkType

    class Base: HandleMarkType {
        override fun handle(value: String): MarkType {
            return when (value) {
                "12" -> MarkType.ControlTest
                "24" -> MarkType.Test
                "17" -> MarkType.Practical
                "16" -> MarkType.Laboratory
                else -> MarkType.Current
            }
        }
    }
}