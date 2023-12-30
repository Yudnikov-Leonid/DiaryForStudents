package com.maxim.diaryforstudents.editDiary.selectClass.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.core.presentation.Reload

interface SelectClassRepository {
    fun init(reload: Reload)
    fun data(): List<ClassData>

    class Base(private val database: DatabaseReference) : SelectClassRepository {
        private val list = mutableListOf<ClassData>()
        override fun init(reload: Reload) {
            database.child("classes").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children.mapNotNull {
                        val name = it.getValue(FirebaseClass::class.java)!!.name
                        ClassData.Base(it.key!!, name)
                    }
                    list.clear()
                    list.addAll(data)
                    reload.reload()
                }

                override fun onCancelled(error: DatabaseError) = reload.error(error.message)
            })
        }

        override fun data() = list.ifEmpty { listOf(ClassData.Empty) }
    }
}

private data class FirebaseClass(val name: String = "")