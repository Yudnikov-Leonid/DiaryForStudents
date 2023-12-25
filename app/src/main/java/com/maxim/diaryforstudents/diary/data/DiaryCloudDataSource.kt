package com.maxim.diaryforstudents.diary.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.maxim.diaryforstudents.core.LessonMapper
import com.maxim.diaryforstudents.news.presentation.Reload
import kotlinx.coroutines.delay

interface DiaryCloudDataSource {
    suspend fun init(reload: Reload)
    fun data(): List<DiaryData.Lesson>

    class Base(
        private val database: DatabaseReference,
        private val mapper: LessonMapper
    ) : DiaryCloudDataSource {
        private val data = mutableListOf<DiaryData.Lesson>()
        override suspend fun init(reload: Reload) {//todo make handleResult
            val classQuery = database.child("users").child(Firebase.auth.uid!!)
            var done = false
            var classId = ""
            classQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    classId = snapshot.getValue(ClassId::class.java)!!.classId
                    done = true
                }

                override fun onCancelled(error: DatabaseError) {
                    classId = error.message
                    done = true
                }
            })
            while (!done)
                delay(50)
            val query = database.child("lessons").orderByChild("classId").equalTo(classId)
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
private data class ClassId(val classId: String = "")
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