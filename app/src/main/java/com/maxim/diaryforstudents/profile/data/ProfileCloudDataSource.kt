package com.maxim.diaryforstudents.profile.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

interface ProfileCloudDataSource {
    fun signOut()

    suspend fun getGrade(callback: ClassNameCallback)
    class Base(
        private val database: DatabaseReference,
        private val clientWrapper: ClientWrapper
    ) : ProfileCloudDataSource {
        override fun signOut() {
            Firebase.auth.signOut()
            clientWrapper.signOut()
        }

        private val classIdCallback = object : ClassIdCallback {
            override fun next(classId: String, callback: ClassNameCallback) {
                val newQuery = database.child("classes").child(classId)
                newQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val className = snapshot.getValue(ClassName::class.java)?.name ?: "error"
                        callback.provide(className)
                    }

                    override fun onCancelled(error: DatabaseError) = callback.provide(error.message)
                })
            }
        }

        override suspend fun getGrade(callback: ClassNameCallback) {
            val query = database.child("users").child(Firebase.auth.uid!!)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val classId = snapshot.getValue(ClassId::class.java)!!.classId
                    classIdCallback.next(classId, callback)
                }

                override fun onCancelled(error: DatabaseError) =
                    classIdCallback.next(error.message, callback)
            })
        }
    }
}

private interface ClassIdCallback {
    fun next(classId: String, callback: ClassNameCallback)
}

private data class ClassId(val classId: String = "")
private data class ClassName(val name: String = "")