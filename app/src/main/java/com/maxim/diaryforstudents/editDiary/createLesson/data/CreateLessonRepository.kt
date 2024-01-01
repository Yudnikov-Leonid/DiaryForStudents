package com.maxim.diaryforstudents.editDiary.createLesson.data

import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.service.CloudLesson
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.core.sl.ManageResource

interface CreateLessonRepository {
    suspend fun create(
        startTime: String,
        endTime: String,
        theme: String,
        homework: String,
        name: String,
        classId: String,
        resource: ManageResource
    ): CreateResult

    suspend fun update(
        date: Int,
        startTime: String,
        endTime: String,
        theme: String,
        homework: String,
        name: String,
        classId: String
    ): CreateResult

    class Base(private val service: Service) : CreateLessonRepository {
        override suspend fun create(
            startTime: String,
            endTime: String,
            theme: String,
            homework: String,
            name: String,
            classId: String,
            resource: ManageResource
        ): CreateResult {
            val date = (System.currentTimeMillis() / 86400000L).toInt()
            val lessonInDatabase =
                service.getOrderByChild("lessons", "date", date.toDouble(), CloudLesson::class.java)
                    .map { it.second }
            if (lessonInDatabase.any { it.classId == classId && it.name == name })
                return CreateResult.Failure(resource.string(R.string.you_have_created_lesson))
            service.pushValue("lessons", CloudLesson(
                classId, date, name, startTime, endTime, theme, homework, (date - 4) / 7
            ))
            return CreateResult.Success
        }

        override suspend fun update(
            date: Int,
            startTime: String,
            endTime: String,
            theme: String,
            homework: String,
            name: String,
            classId: String
        ): CreateResult {
            val lessons =
                service.getOrderByChild("lessons", "date", date.toDouble(), CloudLesson::class.java)
            val lessonId =
                lessons.first { it.second.classId == classId && it.second.name == name }.first
            service.setValue(
                "lessons", lessonId, CloudLesson(
                    classId, date, name, startTime, endTime, theme, homework, (date - 4) / 7
                )
            )
            return CreateResult.Success
        }
    }
}