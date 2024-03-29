package com.maxim.diaryforstudents.diary.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_lessons")
data class RoomLesson(
    val name: String,
    val number: Int,
    val teacherName: String,
    val topic: String,
    val homework: String,
    val previousHomework: String,
    @PrimaryKey
    val startTime: String,
    val endTime: String,
    val date: Int,
)