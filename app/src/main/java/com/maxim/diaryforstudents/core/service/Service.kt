package com.maxim.diaryforstudents.core.service

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.initialize

interface Service {

    fun <T : Any> listen(childOne: String, clasz: Class<T>, listener: ServiceValueEventListener<T>)

    //todo handler will use in news like feature
    class Base(context: Context, private val handler: CoroutineHandler) : Service {
        private val database: DatabaseReference

        init {
            Firebase.database(DATABASE_URL).setPersistenceEnabled(false)
            Firebase.initialize(context)
            database = Firebase.database(DATABASE_URL).reference.root
        }

        override fun <T : Any> listen(
            childOne: String,
            clasz: Class<T>,
            listener: ServiceValueEventListener<T>
        ) {
            database.child(childOne).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listener.valueChanged(snapshot.children.mapNotNull {
                        Pair(it.key!!, it.getValue(clasz)!!)
                    })
                }

                override fun onCancelled(error: DatabaseError) = listener.error(error.message)
            })
        }
    }

    companion object {
        private const val DATABASE_URL =
            "https://diary-ee752-default-rtdb.europe-west1.firebasedatabase.app"
    }
}

interface ServiceValueEventListener<T : Any> {
    fun valueChanged(value: List<Pair<String, T>>)
    fun error(message: String)
}