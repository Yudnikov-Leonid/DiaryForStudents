package com.maxim.diaryforstudents.editDiary.edit.data

import android.icu.util.Calendar
import com.maxim.diaryforstudents.core.service.CloudFinalGrade
import com.maxim.diaryforstudents.core.service.CloudGrade
import com.maxim.diaryforstudents.core.service.CloudLesson
import com.maxim.diaryforstudents.core.service.CloudUser
import com.maxim.diaryforstudents.core.service.MyUser
import com.maxim.diaryforstudents.core.service.Service

interface EditDiaryCloudDataSource {
    suspend fun students(classId: String): List<Student>
    suspend fun lessonName(): String
    suspend fun lessons(classId: String, lessonName: String): List<CloudLesson>
    suspend fun finalGrades(lessonName: String): List<Pair<String, CloudFinalGrade>>
    suspend fun grades(
        date: Int,
        lessonName: String,
        quarter: Int,
        students: List<Student>
    ): List<GradeData>

    fun setGrade(
        child: String,
        lessonName: String,
        quarter: Int,
        grade: Int,
        userId: String,
        date: Int
    )

    suspend fun removeGrade(child: String, lessonName: String, userId: String, date: Int)
    class Base(private val service: Service, private val myUser: MyUser) :
        EditDiaryCloudDataSource {
        override suspend fun students(classId: String): List<Student> =
            service.getOrderByChild("users", "classId", classId, CloudUser::class.java)
                .map { Student(it.second.classId, it.first, it.second.name) }

        override suspend fun lessonName() =
            service.getOrderByKey("users", myUser.id(), CloudUser::class.java).first().second.lesson

        override suspend fun lessons(classId: String, lessonName: String): List<CloudLesson> {
            val calendar = Calendar.getInstance()
            val quarter = when (calendar.get(Calendar.DAY_OF_YEAR)) {
                in 0..91 -> 0..91
                in 92..242 -> 92..242
                in 243..305 -> 243..305
                else -> 306..366
            }
            return service.getOrderByChild("lessons", "classId", classId, CloudLesson::class.java)
                .map { it.second }.filter {
                    calendar.timeInMillis = it.date * 86400000L
                    it.name == lessonName && calendar.get(Calendar.DAY_OF_YEAR) in quarter
                }
        }

        override suspend fun finalGrades(lessonName: String) =
            service.getOrderByChild(
                "final-grades",
                "lesson",
                lessonName,
                CloudFinalGrade::class.java
            )

        override suspend fun grades(
            date: Int,
            lessonName: String,
            quarter: Int,
            students: List<Student>
        ): List<GradeData> {
            val grades = mutableListOf<CloudGrade>()
            students.forEach { student ->
                val list = service.getOrderByChild(
                    "grades",
                    "date",
                    date.toDouble(),
                    CloudGrade::class.java
                ).map { it.second }
                val item = list.filter { it.userId == student.userId && it.lesson == lessonName }
                if (item.isEmpty())
                    grades.add(CloudGrade(date, null, lessonName, quarter, student.userId))
                else
                    grades.add(item.first())
            }
            return grades.map { GradeData.Base(it.date, it.userId, it.grade) }
        }

        override fun setGrade(
            child: String,
            lessonName: String,
            quarter: Int,
            grade: Int,
            userId: String,
            date: Int
        ) = service.pushValueAsync(
            child,
            CloudGrade(date, grade, lessonName, if (date in 100..400) null else quarter, userId)
        )

        override suspend fun removeGrade(
            child: String,
            lessonName: String,
            userId: String,
            date: Int
        ) {
            val id = service.getOrderByChild(child, "date", date.toDouble(), CloudGrade::class.java)
                .first { it.second.userId == userId && it.second.lesson == lessonName }.first
            service.removeAsync(child, id)
        }
    }
}