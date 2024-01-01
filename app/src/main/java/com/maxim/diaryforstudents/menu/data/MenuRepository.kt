package com.maxim.diaryforstudents.menu.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.menu.domain.UserStatus
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface MenuRepository {
    suspend fun getUserStatus(): UserStatus

    class Base(private val dataBase: DatabaseReference) : MenuRepository {
        override suspend fun getUserStatus(): UserStatus {
            val query = dataBase.child("users").child(Firebase.auth.uid!!)
            return when (handle(query)) {
                "student", "" -> UserStatus.Student
                "teacher" -> UserStatus.Teacher
                else -> throw IllegalStateException("unknown user status")
            }
        }

        private suspend fun handle(query: Query): String = suspendCoroutine { cont ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(Status::class.java)!!.status
                    cont.resume(data)
                }

                override fun onCancelled(error: DatabaseError) = cont.resume(error.message)
            })
        }
    }
}

private data class Status(var status: String = "")