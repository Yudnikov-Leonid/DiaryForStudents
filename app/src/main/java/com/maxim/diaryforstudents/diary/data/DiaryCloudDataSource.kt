package com.maxim.diaryforstudents.diary.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.core.LessonMapper
import com.maxim.diaryforstudents.news.presentation.Reload

interface DiaryCloudDataSource {
    fun init(reload: Reload)
    fun data(): List<DiaryData.Lesson>

    class Base(
        private val database: DatabaseReference,
        private val mapper: LessonMapper
    ) : DiaryCloudDataSource {
        private val data = mutableListOf<DiaryData.Lesson>()
        override fun init(reload: Reload) {
            val query = database.child("lessons")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.mapNotNull {
                        it.getValue(CloudDay::class.java)!!
                    }

                    data.clear()
                    data.addAll(list.sortedBy { it.startTime }.map { it.toData(mapper) })
                    reload.reload()
                }

                override fun onCancelled(error: DatabaseError) = reload.error(error.message)
            })
        }

        override fun data(): List<DiaryData.Lesson> = data
    }
}

private data class CloudDay(
    val name: String = "error",
    val theme: String = "",
    val homework: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val date: Int = 0
) {
    fun toData(nameMapper: LessonMapper) =
        DiaryData.Lesson(nameMapper.map(name), theme, homework, startTime, endTime, date)
}