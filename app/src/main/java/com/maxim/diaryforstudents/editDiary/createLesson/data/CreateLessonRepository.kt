package com.maxim.diaryforstudents.editDiary.createLesson.data

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.sl.ManageResource
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    class Base(private val database: DatabaseReference) : CreateLessonRepository {
        override suspend fun create(
            startTime: String,
            endTime: String,
            theme: String,
            homework: String,
            name: String,
            classId: String,
            resource: ManageResource
        ): CreateResult {
            val ref = database.child("lessons").push()
            val date = (System.currentTimeMillis() / 86400000L).toInt()
            val lessonInDatabase =
                handleQuery(
                    database.child("lessons").orderByChild("date").equalTo(date.toDouble())
                ).map { it.second }
            if (lessonInDatabase.filter { it.classId == classId && it.name == name }
                    .isNotEmpty()) return CreateResult.Failure(resource.string(R.string.you_have_created_lesson))

            val lesson = Lesson(
                name, classId, date, startTime, endTime, theme, homework, (date - 4) / 7
            )
            handleTask(ref.setValue(lesson))
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
                handleQuery(database.child("lessons").orderByChild("date").equalTo(date.toDouble()))
            val lessonId =
                lessons.first { it.second.classId == classId && it.second.name == name }.first
            val task = database.child("lessons").child(lessonId).setValue(
                Lesson(
                    name, classId, date, startTime, endTime, theme, homework, (date - 4) / 7
                )
            )
            handleTask(task)
            return CreateResult.Success
        }

        private suspend fun handleTask(task: Task<Void>): Unit = suspendCoroutine { cont ->
            task.addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener {
                cont.resumeWithException(it)
            }
        }

        private suspend fun handleQuery(query: Query): List<Pair<String, Lesson>> =
            suspendCoroutine { cont ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data = snapshot.children.mapNotNull {
                            Pair(it.key!!, it.getValue(Lesson::class.java)!!)
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
    val name: String = "",
    val classId: String = "",
    val date: Int = 0,
    val startTime: String = "",
    val endTime: String = "",
    val theme: String = "",
    val homework: String = "",
    val week: Int = 0
)