package com.maxim.diaryforstudents.fakes

import com.maxim.diaryforstudents.core.data.LessonMapper

class FakeLessonMapper(private val addValue: String) : LessonMapper {
    override fun map(name: String) = "$name$addValue"
}