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
    suspend fun setValue(childOne: String, childTwo: String, childThree: String, value: Any)
    suspend fun setValue(childOne: String, childTwo: String, value: Any)
    suspend fun <T : Any> get(
        childOne: String,
        childTwo: String,
        clasz: Class<T>
    ): List<Pair<String, T>>

    suspend fun <T : Any> getOrderByChild(
        childOne: String,
        orderByChild: String,
        equalTo: String,
        clasz: Class<T>
    ): List<Pair<String, T>>

    suspend fun <T : Any> getOrderByChild(
        childOne: String,
        orderByChild: String,
        equalTo: Double,
        clasz: Class<T>
    ): List<Pair<String, T>>

    suspend fun <T : Any> getOrderByKey(
        childOne: String,
        equalTo: String,
        clasz: Class<T>
    ): List<Pair<String, T>>

    fun removeAsync(childOne: String, childTwo: String)
    fun pushValueAsync(childOne: String, value: Any)
    suspend fun pushValue(childOne: String, value: Any)
    fun <T : Any> listen(childOne: String, clasz: Class<T>, listener: ServiceValueEventListener<T>)
    fun <T : Any> listen(
        childOne: String,
        childTwo: String,
        clasz: Class<T>,
        listener: ServiceValueEventListener<T>
    )

    fun <T : Any> listenByChild(
        childOne: String,
        orderByChild: String,
        equalTo: String,
        clasz: Class<T>,
        listener: ServiceValueEventListener<T>
    )

    fun <T : Any> listenByChild(
        childOne: String,
        orderByChild: String,
        equalTo: Double,
        clasz: Class<T>,
        listener: ServiceValueEventListener<T>
    )

    class Base(context: Context, private val handler: CoroutineHandler) : Service {
        private val database: DatabaseReference

        init {
            Firebase.database(DATABASE_URL).setPersistenceEnabled(false)
            Firebase.initialize(context)
            database = Firebase.database(DATABASE_URL).reference.root
        }

        override suspend fun setValue(
            childOne: String,
            childTwo: String,
            childThree: String,
            value: Any
        ) = handler.handleResult(database.child(childOne).child(childTwo).child(childThree).setValue(value))

        override suspend fun setValue(childOne: String, childTwo: String, value: Any) =
            handler.handleResult(database.child(childOne).child(childTwo).setValue(value))

        override suspend fun <T : Any> get(
            childOne: String,
            childTwo: String,
            clasz: Class<T>
        ): List<Pair<String, T>> = handler.handleQuery(database.child(childOne).child(childTwo), clasz)

        override suspend fun <T : Any> getOrderByChild(
            childOne: String,
            orderByChild: String,
            equalTo: String,
            clasz: Class<T>
        ): List<Pair<String, T>> =
            handler.handleQuery(database.child(childOne).orderByChild(orderByChild).equalTo(equalTo), clasz)

        override suspend fun <T : Any> getOrderByChild(
            childOne: String,
            orderByChild: String,
            equalTo: Double,
            clasz: Class<T>
        ): List<Pair<String, T>> =
            handler.handleQuery(database.child(childOne).orderByChild(orderByChild).equalTo(equalTo), clasz)

        override suspend fun <T : Any> getOrderByKey(
            childOne: String,
            equalTo: String,
            clasz: Class<T>
        ): List<Pair<String, T>> =
            handler.handleQuery(database.child(childOne).orderByKey().equalTo(equalTo), clasz)

        override fun removeAsync(childOne: String, childTwo: String) {
            database.child(childOne).child(childTwo).removeValue()
        }

        override fun pushValueAsync(childOne: String, value: Any) {
            val ref = database.child(childOne).push()
            ref.setValue(value)
        }

        override suspend fun pushValue(childOne: String, value: Any) {
            val ref = database.child(childOne).push()
            handler.handleResult(ref.setValue(value))
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

        override fun <T : Any> listen(
            childOne: String,
            childTwo: String,
            clasz: Class<T>,
            listener: ServiceValueEventListener<T>
        ) {
            database.child(childOne).child(childTwo)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        listener.valueChanged(snapshot.children.mapNotNull {
                            Pair(it.key!!, it.getValue(clasz)!!)
                        })
                    }

                    override fun onCancelled(error: DatabaseError) = listener.error(error.message)
                })
        }

        override fun <T : Any> listenByChild(
            childOne: String,
            orderByChild: String,
            equalTo: String,
            clasz: Class<T>,
            listener: ServiceValueEventListener<T>
        ) {
            database.child(childOne).orderByChild(orderByChild).equalTo(equalTo)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        listener.valueChanged(snapshot.children.mapNotNull {
                            Pair(it.key!!, it.getValue(clasz)!!)
                        })
                    }

                    override fun onCancelled(error: DatabaseError) = listener.error(error.message)
                })
        }

        override fun <T : Any> listenByChild(
            childOne: String,
            orderByChild: String,
            equalTo: Double,
            clasz: Class<T>,
            listener: ServiceValueEventListener<T>
        ) {
            database.child(childOne).orderByChild(orderByChild).equalTo(equalTo)
                .addValueEventListener(object : ValueEventListener {
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