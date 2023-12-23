package com.maxim.diaryforstudents.login.data

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface LoginCloudDataSource {
    suspend fun login()

    class Base(private val dataBase: DatabaseReference) : LoginCloudDataSource {
        override suspend fun login() {
            val user = Firebase.auth.currentUser
            val uId = user!!.uid
            val email = user.email!!
            val name = user.displayName!!
            val result = dataBase.child("users").child(uId).setValue(UserProfileCloud(email, name))
            handleResult(result)
        }

        private suspend fun handleResult(value: Task<Void>): Unit =
            suspendCoroutine { cont ->
                value.addOnSuccessListener {
                    cont.resume(Unit)
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
            }
    }
}