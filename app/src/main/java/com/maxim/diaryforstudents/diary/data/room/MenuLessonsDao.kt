package com.maxim.diaryforstudents.diary.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MenuLessonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RoomLesson)

    @Query("SELECT * FROM menu_lessons")
    suspend fun lessons(): List<RoomLesson>

    @Query("DELETE FROM menu_lessons")
    suspend fun clear()
}