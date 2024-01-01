package com.maxim.diaryforstudents.editDiary.edit.data

import com.maxim.diaryforstudents.core.data.LessonMapper
import com.maxim.diaryforstudents.editDiary.common.CreateLessonCache
import java.util.Calendar

interface EditDiaryRepository {
    suspend fun init(classId: String): List<LessonData>

    suspend fun setGrade(grade: Int?, userId: String, date: Int)

    class Base(
        private val dataSource: EditDiaryCloudDataSource,
        private val cache: CreateLessonCache.Update,
        private val mapper: LessonMapper
    ) : EditDiaryRepository {
        private var lessonName: String = ""
        private var quarter: Int = 0

        /**
         *  get students by classId, then get lessons by classId and lessonName,
         *  and get grades of every student by lesson's date, userId and lessonName
         */
        override suspend fun init(classId: String): List<LessonData> {
            quarter = when (Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                in 0..91 -> 3
                in 92..242 -> 4
                in 243..305 -> 1
                else -> 2
            }

            val students = dataSource.students(classId)
            lessonName = dataSource.lessonName()
            val lessons = dataSource.lessons(classId, lessonName)

            cache.cacheName(lessonName)
            cache.cacheClassId(classId)

            val result = mutableListOf<LessonData>()
            val studentsData = mutableListOf<StudentData>(StudentData.Title(mapper.map(lessonName)))
            studentsData.addAll(students.map { StudentData.Base(it.name) })
            result.add(LessonData.Students(studentsData))

            lessons.sortedBy { it.date }.forEach { lesson ->
                val gradesData = mutableListOf<GradeData>(
                    GradeData.Date(
                        lesson.date,
                        lesson.startTime,
                        lesson.endTime,
                        lesson.theme,
                        lesson.homework
                    )
                )
                gradesData.addAll(dataSource.grades(lesson.date, lessonName, quarter, students))
                result.add(LessonData.Lesson(gradesData))
            }

            val final = mutableListOf<GradeData>(GradeData.FinalTitle)
            val finalGrades = dataSource.finalGrades(lessonName)
            students.forEach { student ->
                val grades =
                    finalGrades.filter { it.second.userId == student.userId && it.second.date == quarter * 100 }
                if (grades.isNotEmpty())
                    final.add(
                        GradeData.Base(
                            quarter * 100,
                            student.userId,
                            grades.first().second.grade
                        )
                    )
                else
                    final.add(GradeData.Base(quarter * 100, student.userId, null))
            }
            result.add(LessonData.Lesson(final))
            return result
        }

        override suspend fun setGrade(grade: Int?, userId: String, date: Int) {
            val child = if (date in 100..400) "final-grades" else "grades"
            grade?.let {
                dataSource.setGrade(child, lessonName, quarter, it, userId, date)
            } ?: dataSource.removeGrade(child, lessonName, userId, date)
        }
    }
}

data class Student(val classId: String = "", val userId: String = "", val name: String = "")