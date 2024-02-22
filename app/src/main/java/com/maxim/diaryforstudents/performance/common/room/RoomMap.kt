package com.maxim.diaryforstudents.performance.common.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checked_marks")
data class MarkRoom (
    @PrimaryKey
    val id: String,
    val value: Int,
    val date: String,
    val lessonName: String,
    val markType: String?
)