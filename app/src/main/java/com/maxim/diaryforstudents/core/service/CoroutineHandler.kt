package com.maxim.diaryforstudents.core.service

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface CoroutineHandler {
    suspend fun <T : Any> handleQuery(
        query: Query,
        clasz: Class<T>
    ): List<Pair<String, T>>

    suspend fun handleResult(value: Task<Void>)
    class Base : CoroutineHandler {
        override suspend fun <T : Any> handleQuery(
            query: Query,
            clasz: Class<T>
        ): List<Pair<String, T>> =
            suspendCoroutine { cont ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.childrenCount == 0L)
                            cont.resume(emptyList())
                        else if (snapshot.children.first().hasChildren()) {
                            val data = snapshot.children.mapNotNull {
                                Pair(it.key!!, it.getValue(clasz)!!)
                            }
                            cont.resume(data)
                        } else
                            cont.resume(listOf(Pair(snapshot.key!!, snapshot.getValue(clasz)!!)))
                    }

                    override fun onCancelled(error: DatabaseError) =
                        cont.resumeWithException(error.toException())
                })
            }

        override suspend fun handleResult(value: Task<Void>): Unit =
            suspendCoroutine { cont ->
                value.addOnSuccessListener {
                    cont.resume(Unit)
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
            }
    }
}