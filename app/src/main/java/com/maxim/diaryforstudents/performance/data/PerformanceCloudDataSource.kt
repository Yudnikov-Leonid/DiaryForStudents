package com.maxim.diaryforstudents.performance.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.core.data.LessonMapper
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.service.MyUser
import com.maxim.diaryforstudents.performance.presentation.PerformanceViewModel

interface PerformanceCloudDataSource {
    fun init(reload: Reload)
    fun data(quarter: Int): List<PerformanceData.Lesson>
    fun changeType(type: String)

    class Base(
        private val database: DatabaseReference,
        private val myUser: MyUser,
        private val mapper: LessonMapper
    ) : PerformanceCloudDataSource {
        private val data = mutableListOf<Lesson>()
        private var type = PerformanceViewModel.ACTUAL
        private val queries = mutableListOf<Pair<Query, ValueEventListener>>()
        override fun init(reload: Reload) {
            val uId = myUser.id()
            val query = database.child(type).orderByChild("userId").equalTo(uId)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val grades = snapshot.children.mapNotNull {
                        it.getValue(Lesson::class.java)
                    }
                    data.clear()
                    data.addAll(grades)
                    reload.reload()
                }

                override fun onCancelled(error: DatabaseError) = reload.error(error.message)
            }
            query.addValueEventListener(listener)
            queries.add(Pair(query, listener))
        }

        override fun data(quarter: Int): List<PerformanceData.Lesson> {
            val result = mutableListOf<PerformanceData.Lesson>()
            val grades: List<Lesson> =
                if (type == PerformanceViewModel.ACTUAL) data.filter { it.quarter == quarter } else data
            val map = mutableMapOf<String, MutableList<Grade>>()
            grades.forEach {
                if (map[it.lesson] == null)
                    map[it.lesson] = ArrayList()
                map[it.lesson]!!.add(Grade(it.grade, it.date))
            }
            map.toSortedMap().forEach {
                val average = (it.value.sumOf { it.grade }).toFloat() / it.value.size
                it.value.sortBy { it.date }
                result.add(
                    PerformanceData.Lesson(
                        mapper.map(it.key),
                        it.value.map { it.toData() },
                        average
                    )
                )
            }
            return result
        }

        override fun changeType(type: String) {
            queries.forEach {
                it.first.removeEventListener(it.second)
            }
            queries.clear()
            this.type = type
        }
    }
}

private data class Lesson(
    val grade: Int = 0,
    val lesson: String = "",
    val userId: String = "",
    val quarter: Int = 0,
    val date: Int = 0
)

private data class Grade(
    val grade: Int,
    val date: Int
) {
    fun toData() = PerformanceData.Grade(grade, date)
}