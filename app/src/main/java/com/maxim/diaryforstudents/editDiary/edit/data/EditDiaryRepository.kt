package com.maxim.diaryforstudents.editDiary.edit.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.core.data.LessonMapper
import com.maxim.diaryforstudents.editDiary.common.CreateLessonCache
import com.maxim.diaryforstudents.editDiary.edit.presentation.ReloadAfterDismiss
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface EditDiaryRepository {
    suspend fun init(classId: String): List<LessonData>

    fun setGrade(grade: Int?, userId: String, date: Int)

    fun cacheUpdateAfterDismiss(reloadAfterDismiss: ReloadAfterDismiss)

    class Base(
        private val database: DatabaseReference,
        private val cache: CreateLessonCache.Update,
        private val mapper: LessonMapper
    ) : EditDiaryRepository {
        private var lessonName: String = ""
        private var quarter: Int = 0
        override suspend fun init(classId: String): List<LessonData> {
            // get students by classId, then get lessons by classId and lessonName,
            // and get grades of every student by lesson's date, userId, and lessonName

            val formatter = SimpleDateFormat("DDD") //todo formatter
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            quarter = when (formatter.format(calendar.time).toInt()) {
                in 0..91 -> 3
                in 92..242 -> 4
                in 243..305 -> 1
                else -> 2
            }

            val students = handleQuery(
                database.child("users").orderByChild("classId").equalTo(classId),
                Student::class.java
            ).map { Student(it.second.classId, it.first, it.second.name) }
            lessonName = handleQuery(
                database.child("users").orderByKey().equalTo(Firebase.auth.uid!!),
                TeacherLessonName::class.java
            ).map { it.second }.first().lesson
            val lessons = handleQuery(
                database.child("lessons").orderByChild("classId").equalTo(classId),
                Lesson::class.java
            ).map { it.second }.filter { it.name == lessonName }

            cache.cacheName(lessonName)
            cache.cacheClassId(classId)

            val result = mutableListOf<LessonData>()
            val studentsData = mutableListOf<StudentData>(StudentData.Title(mapper.map(lessonName)))
            studentsData.addAll(students.map { StudentData.Base(it.name) })
            result.add(LessonData.Students(studentsData))
            lessons.sortedBy { it.date }.forEach { lesson ->
                val gradesData = mutableListOf<GradeData>(GradeData.Date(lesson.date))
                gradesData.addAll(grades(lesson.date, students, lessonName))
                result.add(LessonData.Lesson(gradesData))
            }
            return result
        }

        override fun setGrade(grade: Int?, userId: String, date: Int) {
            if (grade != null) {
                val ref = database.child("grades").push()
                val firebaseGrade = Grade(date, grade, lessonName, quarter, userId)
                ref.setValue(firebaseGrade)
            } else {
                database.child("grades").orderByChild("date").equalTo(date.toDouble())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val list = snapshot.children.mapNotNull {
                                Pair(it.key!!, it.getValue(Grade::class.java)!!)
                            }
                            val id =
                                list.filter { it.second.userId == userId && it.second.lesson == lessonName }
                                    .first().first
                            database.child("grades").child(id).removeValue()
                        }

                        override fun onCancelled(error: DatabaseError) = Unit //todo
                    })
            }
        }

        override fun cacheUpdateAfterDismiss(reloadAfterDismiss: ReloadAfterDismiss) {
            cache.cacheAfterDismiss(reloadAfterDismiss)
        }

        private suspend fun grades(
            date: Int,
            students: List<Student>,
            lesson: String
        ): List<GradeData> {
            val grades = mutableListOf<Grade>()
            students.forEach { student ->
                val list = handleQuery(
                    database.child("grades").orderByChild("date")
                        .equalTo(date.toDouble()),
                    Grade::class.java
                ).map { it.second }
                val item = list.filter { it.userId == student.userId && it.lesson == lesson }
                if (item.isEmpty())
                    grades.add(Grade(date, null, lessonName, quarter, student.userId))
                else
                    grades.add(item.first())
            }
            return grades.map { GradeData.Base(it.date, it.userId, it.grade) }
        }


        private suspend fun <T : Any> handleQuery(
            query: Query,
            clasz: Class<T>
        ): List<Pair<String, T>> =
            suspendCoroutine { cont ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data = snapshot.children.mapNotNull {
                            Pair(it.key!!, it.getValue(clasz)!!)
                        }
                        cont.resume(data)
                    }

                    override fun onCancelled(error: DatabaseError) =
                        cont.resumeWithException(error.toException())
                })
            }
    }
}

private data class Grade(
    val date: Int = 0,
    val grade: Int? = null,
    val lesson: String = "",
    val quarter: Int = 0,
    val userId: String = ""
)

private data class Lesson(
    val date: Int = 0,
    val startTime: String = "",
    val endTime: String = "",
    val name: String = "",
    val theme: String = "",
    val homework: String = ""
)

private data class TeacherLessonName(val lesson: String = "")
private data class Student(val classId: String = "", val userId: String = "", val name: String = "")