package com.maxim.diaryforstudents.diary.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.core.data.LessonMapper
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.service.MyUser

interface DiaryCloudDataSource {
    suspend fun init(reload: Reload, week: Int)
    fun data(): List<DiaryData.Lesson>

    class Base(
        private val database: DatabaseReference,
        private val myUser: MyUser,
        private val mapper: LessonMapper
    ) : DiaryCloudDataSource {
        private val data = mutableListOf<DiaryData.Lesson>()
        private val listeners = mutableListOf<Pair<Query, ValueEventListener>>()
        private val callback = object : ClassIdCallback {
            override fun next(classId: String, reload: Reload, week: Int) {
                val query = database.child("lessons").orderByChild("week").equalTo(week.toDouble())
                val listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = snapshot.children.mapNotNull {
                            it.getValue(CloudDay::class.java)!!
                        }
                        data.clear()
                        data.addAll(list.filter { it.classId == classId }.sortedBy { it.startTime }
                            .map { it.toData(mapper) })
                        reload.reload()
                    }

                    override fun onCancelled(error: DatabaseError) = reload.error(error.message)
                }
                query.addValueEventListener(listener)
                listeners.add(Pair(query, listener))
            }
        }

        override suspend fun init(reload: Reload, week: Int) {
            if (listeners.isNotEmpty()) {
                listeners.forEach {
                    it.first.removeEventListener(it.second)
                }
                listeners.clear()
            }

            val classQuery = database.child("users").child(myUser.id())

            classQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val classId = snapshot.getValue(ClassId::class.java)!!.classId
                    callback.next(classId, reload, week)
                }

                override fun onCancelled(error: DatabaseError) = reload.error(error.message)
            })
        }

        override fun data(): List<DiaryData.Lesson> = data
    }
}

private interface ClassIdCallback {
    fun next(classId: String, reload: Reload, week: Int)
}

private data class ClassId(val classId: String = "")
private data class CloudDay(
    val classId: String = "",
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