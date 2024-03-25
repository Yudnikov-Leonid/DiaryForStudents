package com.maxim.diaryforstudents.performance.common.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PerformanceDao {
    @Query("DELETE FROM checked_marks")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: MarkRoom)

    @Query("SELECT * FROM checked_marks")
    suspend fun checkedMarks(): List<MarkRoom>
}