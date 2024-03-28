package com.maxim.diaryforstudents.diary.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomLesson::class], version = 1)
abstract class MenuLessonsDatabase: RoomDatabase() {
    abstract fun dao(): MenuLessonsDao
}