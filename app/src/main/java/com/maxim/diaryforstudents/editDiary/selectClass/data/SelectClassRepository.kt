package com.maxim.diaryforstudents.editDiary.selectClass.data

import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.service.CloudClass
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.core.service.ServiceValueEventListener

interface SelectClassRepository {
    fun init(reload: Reload)
    fun data(): List<ClassData>

    class Base(private val service: Service) : SelectClassRepository {
        private val list = mutableListOf<ClassData>()
        override fun init(reload: Reload) {
            service.listen(
                "classes",
                CloudClass::class.java,
                object : ServiceValueEventListener<CloudClass> {
                    override fun valueChanged(value: List<Pair<String, CloudClass>>) {
                        list.clear()
                        list.addAll(value.map { ClassData.Base(it.first, it.second.name) })
                        reload.reload()
                    }

                    override fun error(message: String) = reload.error(message)
                })
        }

        override fun data() = list.ifEmpty { listOf(ClassData.Empty) }
    }
}