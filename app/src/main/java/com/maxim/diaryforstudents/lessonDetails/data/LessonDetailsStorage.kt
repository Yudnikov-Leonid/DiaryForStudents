package com.maxim.diaryforstudents.lessonDetails.data

import com.maxim.diaryforstudents.diary.presentation.DiaryUi

interface LessonDetailsStorage {
    interface Save {
        fun save(value: DiaryUi.Lesson)
    }

    interface Read {
        fun lesson(): DiaryUi.Lesson
    }

    interface Mutable : Save, Read

    class Base : Mutable {
        private lateinit var cache: DiaryUi.Lesson

        override fun save(value: DiaryUi.Lesson) {
            cache = value
        }

        override fun lesson() = cache
    }
}