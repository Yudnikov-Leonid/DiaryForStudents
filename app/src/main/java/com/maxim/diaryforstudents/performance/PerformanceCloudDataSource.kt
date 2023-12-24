package com.maxim.diaryforstudents.performance

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.news.Reload

interface PerformanceCloudDataSource {
    fun init(reload: Reload)
    fun data(quarter: Int): List<PerformanceData.Lesson>

    class Base(private val database: DatabaseReference) : PerformanceCloudDataSource {
        private val data = mutableListOf<Lesson>()
        override fun init(reload: Reload) {
            val uId = Firebase.auth.currentUser!!.uid
            val query = database.child("grades").orderByChild("userId").equalTo(uId)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val grades = snapshot.children.mapNotNull {
                        it.getValue(Lesson::class.java)
                    }
                    data.clear()
                    data.addAll(grades)
                    reload.reload()
                }

                override fun onCancelled(error: DatabaseError) = reload.error(error.message)
            })
        }

        override fun data(quarter: Int): List<PerformanceData.Lesson> {
            val result = mutableListOf<PerformanceData.Lesson>()
            val grades = data.filter { it.quarter == quarter }
            val map = mutableMapOf<String, MutableList<Grade>>()
            grades.forEach {
                if (map[it.lesson] == null)
                    map[it.lesson] = ArrayList()
                map[it.lesson]!!.add(Grade(it.grade, it.date))
            }
            map.forEach {
                val average = (it.value.sumOf { it.grade }).toFloat() / it.value.size
                it.value.sortBy { it.date }
                result.add(PerformanceData.Lesson(it.key, it.value.map { it.toData() }, average))
            }
            return result
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