package com.maxim.diaryforstudents.performance.common.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MarkRoom::class], version = 1, exportSchema = false)
abstract class PerformanceDatabase : RoomDatabase() {
    abstract fun dao(): PerformanceDao
}