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
    fun data(): List<LessonData>

    class Base(
        private val database: DatabaseReference,
        private val mapper: LessonMapper
    ) : EditDiaryRepository {
        private val list = mutableListOf<LessonData>()
        override suspend fun init(classId: String): List<LessonData> {
            // get students by classId, then get lessons by classId and lessonName,
            // and get grades of every student by lesson's date, userId, and lessonName

            val students = studentsHandleQuery(
                database.child("users").orderByChild("status").equalTo("student")
            ).filter { it.classId == classId }
            val lessonName = lessonNameHandleQuery(
                database.child("users").orderByKey().equalTo(Firebase.auth.uid!!)
            )
            val lessons = lessonsHandleQuery(
                database.child("lessons").orderByChild("classId").equalTo(classId)
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

        private suspend fun lessonsHandleQuery(query: Query): List<Lesson> =
            suspendCoroutine { cont ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data = snapshot.children.mapNotNull {
                            it.getValue(Lesson::class.java)
                        }
                        cont.resume(data)
                    }

                    override fun onCancelled(error: DatabaseError) =
                        cont.resumeWithException(error.toException())
                })
            }

        private suspend fun lessonNameHandleQuery(query: Query): String =
            suspendCoroutine { cont ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data =
                            snapshot.children.mapNotNull { it.getValue(TeacherLessonName::class.java) }
                        cont.resume(data.first().lesson)
                    }

                    override fun onCancelled(error: DatabaseError) =
                        cont.resumeWithException(error.toException())
                })
            }

        private suspend fun studentsHandleQuery(query: Query): List<Student> =
            suspendCoroutine { cont ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data = snapshot.children.mapNotNull {
                            val value = it.getValue(Student::class.java)!!
                            Student(value.classId, it.key!!, value.name)
                        }
                        cont.resume(data)
                    }

                    override fun onCancelled(error: DatabaseError) =
                        cont.resumeWithException(error.toException())
                })
            }

        override fun data() = list
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