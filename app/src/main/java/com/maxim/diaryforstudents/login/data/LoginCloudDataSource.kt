package com.maxim.diaryforstudents.login.data

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.maxim.diaryforstudents.core.service.MyUser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface LoginCloudDataSource {
    suspend fun login()

    class Base(
        private val dataBase: DatabaseReference,
        private val myUser: MyUser
    ) : LoginCloudDataSource {
        override suspend fun login() {
            val uId = myUser.id()
            val email = myUser.email()
            val name = myUser.name()
            var result = dataBase.child("users").child(uId).child("email").setValue(email)
            handleResult(result)
            result = dataBase.child("users").child(uId).child("name").setValue(name)
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