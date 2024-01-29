package com.maxim.diaryforstudents.calculateAverage.data

import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

interface CalculateStorage {
    interface Save {
        fun save(marks: List<PerformanceUi.Mark>, marksSum: Int)
    }

    interface Read {
        fun marks(): List<PerformanceUi.Mark>
        fun sum(): Int
    }

    interface Mutable: Save, Read

    class Base: Mutable {
        private val list = mutableListOf<PerformanceUi.Mark>()
        private var sum = 0

        override fun save(marks: List<PerformanceUi.Mark>, marksSum: Int) {
            list.clear()
            list.addAll(marks)
            sum = marksSum
        }

        override fun marks() = list

        override fun sum() = sum
    }
}