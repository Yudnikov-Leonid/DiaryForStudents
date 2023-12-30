package com.maxim.diaryforstudents.editDiary.edit.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.core.data.LessonMapper
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface EditDiaryRepository {
    suspend fun init(classId: String): List<LessonData>

    class Base(
        private val database: DatabaseReference,
        private val mapper: LessonMapper
    ) : EditDiaryRepository {
        private val list = mutableListOf<LessonData>()
        override suspend fun init(classId: String): List<LessonData> {
            // get students by classId, then get lessons by classId and lessonName,
            // and get grades of every student by lesson's date, userId, and lessonName

            val students = handleQuery(
                database.child("users").orderByChild("classId").equalTo(classId),
                Student::class.java
            )
            val lessonName = handleQuery(
                database.child("users").orderByKey().equalTo(Firebase.auth.uid!!),
                TeacherLessonName::class.java
            ).first().lesson
            val lessons = handleQuery(
                database.child("lessons").orderByChild("classId").equalTo(classId),
                Lesson::class.java
            ).filter { it.name == lessonName }

            val result = mutableListOf<LessonData>()
            val studentsData = mutableListOf<StudentData>(StudentData.Title(mapper.map(lessonName)))
            studentsData.addAll(students.sortedBy { it.name }.map { StudentData.Base(it.name) })
            result.add(LessonData.Students(studentsData))
            lessons.sortedBy { it.date }.forEach {
                val gradesData = mutableListOf<GradeData>(GradeData.Date(it.date))
                gradesData.addAll(List(students.size) { _ -> GradeData.Base(null) }) //todo
                result.add(LessonData.Lesson(gradesData))
            }
            return result
        }

        private suspend fun <T : Any> handleQuery(query: Query, clasz: Class<T>): List<T> =
            suspendCoroutine { cont ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data = snapshot.children.mapNotNull {
                            it.getValue(clasz)
                        }
                        cont.resume(data)
                    }

                    override fun onCancelled(error: DatabaseError) =
                        cont.resumeWithException(error.toException())
                })
            }
    }
}

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