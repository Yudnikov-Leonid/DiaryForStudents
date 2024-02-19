package com.maxim.diaryforstudents.calculateAverage.data

import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

interface CalculateStorage {
    interface Save {
        fun save(marks: List<PerformanceUi>, marksSum: Int)
    }

    interface Read {
        fun marks(): List<PerformanceUi>
        fun sum(): Int
    }

    interface Mutable: Save, Read

    class Base: Mutable {
        private val list = mutableListOf<PerformanceUi>()
        private var sum = 0

        override fun save(marks: List<PerformanceUi>, marksSum: Int) {
            list.clear()
            list.addAll(marks)
            sum = marksSum
        }

        override fun marks() = list

        override fun sum() = sum
    }
}