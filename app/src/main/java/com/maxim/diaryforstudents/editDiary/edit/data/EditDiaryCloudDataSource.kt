package com.maxim.diaryforstudents.editDiary.edit.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface EditDiaryCloudDataSource {
    suspend fun students(classId: String): List<Student>
    suspend fun lessonName(): String
    suspend fun lessons(classId: String, lessonName: String): List<Lesson>
    suspend fun finalGrades(lessonName: String): List<Pair<String, Grade>>
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

    fun removeGrade(child: String, lessonName: String, userId: String, date: Int)

    class Base(private val database: DatabaseReference) : EditDiaryCloudDataSource {
        override suspend fun students(classId: String): List<Student> = handleQuery(
            database.child("users").orderByChild("classId").equalTo(classId),
            Student::class.java
        ).map { Student(it.second.classId, it.first, it.second.name) }

        override suspend fun lessonName() = handleQuery(
            database.child("users").orderByKey().equalTo(Firebase.auth.uid!!),
            TeacherLessonName::class.java
        ).map { it.second }.first().lesson

        override suspend fun lessons(classId: String, lessonName: String) = handleQuery(
            database.child("lessons").orderByChild("classId").equalTo(classId),
            Lesson::class.java
        ).map { it.second }.filter { it.name == lessonName }

        override suspend fun finalGrades(lessonName: String) = handleQuery(
            database.child("final-grades").orderByChild("lesson").equalTo(lessonName),
            Grade::class.java
        )

        override suspend fun grades(
            date: Int,
            lessonName: String,
            quarter: Int,
            students: List<Student>
        ): List<GradeData> {
            val grades = mutableListOf<Grade>()
            students.forEach { student ->
                val list = handleQuery(
                    database.child("grades").orderByChild("date")
                        .equalTo(date.toDouble()),
                    Grade::class.java
                ).map { it.second }
                val item = list.filter { it.userId == student.userId && it.lesson == lessonName }
                if (item.isEmpty())
                    grades.add(Grade(date, null, lessonName, quarter, student.userId))
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
        ) {
            val ref = database.child(child).push()
            val firebaseGrade =
                Grade(date, grade, lessonName, if (date in 100..400) null else quarter, userId)
            ref.setValue(firebaseGrade)
        }

        override fun removeGrade(child: String, lessonName: String, userId: String, date: Int) {
            database.child(child).orderByChild("date").equalTo(date.toDouble())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = snapshot.children.mapNotNull {
                            Pair(it.key!!, it.getValue(Grade::class.java)!!)
                        }
                        val id = list.first {
                            it.second.userId == userId && it.second.lesson == lessonName }.first
                        database.child(child).child(id).removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) = Unit
                })
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