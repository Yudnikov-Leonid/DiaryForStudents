package com.maxim.diaryforstudents.profile.data

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

interface ProfileCloudDataSource {
    fun signOut()

    suspend fun getGrade(): GradeResult
    class Base(
        private val database: DatabaseReference,
        private val clientWrapper: ClientWrapper,
        private val lessonMapper: LessonMapper
    ) : ProfileCloudDataSource {
        override fun signOut() {
            Firebase.auth.signOut()
            clientWrapper.signOut()
        }

        override suspend fun getGrade(): GradeResult {
            val user = handleQuery(
                database.child("users").child(Firebase.auth.uid!!),
                ClassId::class.java
            )
            return if (user.lesson != "") {
                GradeResult.Teacher(lessonMapper.map(user.lesson))
            } else if (user.classId != "") {
                GradeResult.Student(
                    handleQuery(
                        database.child("classes").child(user.classId),
                        ClassName::class.java
                    ).name
                )
            } else GradeResult.Empty
        }

        private suspend fun <T : Any> handleQuery(query: Query, clasz: Class<T>): T =
            suspendCoroutine { cont ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        cont.resume(snapshot.getValue(clasz)!!)
                    }

                    override fun onCancelled(error: DatabaseError) =
                        cont.resumeWithException(error.toException())
                })
            }
    }
}

private data class ClassId(val classId: String = "", val lesson: String = "")
private data class ClassName(val name: String = "")