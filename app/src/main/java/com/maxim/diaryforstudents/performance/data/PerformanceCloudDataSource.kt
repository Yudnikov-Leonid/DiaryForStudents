package com.maxim.diaryforstudents.performance.data

import com.maxim.diaryforstudents.core.data.LessonMapper
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.service.CloudGrade
import com.maxim.diaryforstudents.core.service.MyUser
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.core.service.ServiceValueEventListener
import com.maxim.diaryforstudents.performance.presentation.PerformanceViewModel

interface PerformanceCloudDataSource {
    fun init(reload: Reload)
    fun data(quarter: Int): List<PerformanceData.Lesson>
    fun changeType(type: String)

    class Base(
        private val service: Service,
        private val myUser: MyUser,
        private val mapper: LessonMapper
    ) : PerformanceCloudDataSource {
        private val data = mutableListOf<CloudGrade>()
        private var type = PerformanceViewModel.ACTUAL
        override fun init(reload: Reload) {
            service.listenByChild(type, "userId", myUser.id(), CloudGrade::class.java,
                object : ServiceValueEventListener<CloudGrade> {
                    override fun valueChanged(value: List<Pair<String, CloudGrade>>) {
                        data.clear()
                        data.addAll(value.map { it.second })
                        reload.reload()
                    }

                    override fun error(message: String) = reload.error(message)
                })
        }

        override fun data(quarter: Int): List<PerformanceData.Lesson> {
            val result = mutableListOf<PerformanceData.Lesson>()
            val grades: List<CloudGrade> =
                if (type == PerformanceViewModel.ACTUAL) data.filter { it.quarter == quarter } else data
            val map = mutableMapOf<String, MutableList<CloudGrade>>()
            grades.forEach {
                if (map[it.lesson] == null)
                    map[it.lesson] = mutableListOf()
                map[it.lesson]!!.add(CloudGrade(it.date, it.grade))
            }
            map.toSortedMap().forEach {
                val average = (it.value.sumOf { it.grade!! }).toFloat() / it.value.size
                it.value.sortBy { it.date }
                result.add(
                    PerformanceData.Lesson(
                        mapper.map(it.key),
                        it.value.map { PerformanceData.Grade(it.grade!!, it.date) },
                        average
                    )
                )
            }
            return result
        }

        override fun changeType(type: String) {
            this.type = type
        }
    }
}