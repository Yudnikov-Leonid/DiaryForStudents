package com.maxim.diaryforstudents.diary.data.room

import com.maxim.diaryforstudents.diary.data.DiaryData
import com.maxim.diaryforstudents.diary.data.MenuLessonState
import com.maxim.diaryforstudents.performance.common.data.PerformanceData
import java.lang.IllegalStateException

class DiaryDataToRoomMapper: DiaryData.Mapper<RoomLesson> {
    override fun map(date: Int, lessons: List<DiaryData>) = throw IllegalStateException("can't map DiaryData to RoomLesson")

    override fun map(
        name: String,
        number: Int,
        teacherName: String,
        topic: String,
        homework: String,
        previousHomework: String,
        startTime: String,
        endTime: String,
        date: Int,
        marks: List<PerformanceData.Mark>,
        absence: List<String>,
        notes: List<String>,
        menuState: MenuLessonState?
    ): RoomLesson = RoomLesson(name, number, teacherName, topic, homework, previousHomework, startTime, endTime, date)

    override fun map() = throw IllegalStateException("can't map DiaryData to RoomLesson")
}