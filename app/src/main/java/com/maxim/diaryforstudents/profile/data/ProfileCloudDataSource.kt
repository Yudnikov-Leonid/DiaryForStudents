package com.maxim.diaryforstudents.profile.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay

interface ProfileCloudDataSource {
    fun signOut()

    suspend fun getGrade(): String
    class Base(
        private val database: DatabaseReference,
        private val clientWrapper: ClientWrapper
    ) :
        ProfileCloudDataSource {
        override fun signOut() {
            Firebase.auth.signOut()
            clientWrapper.signOut()
        }

        override suspend fun getGrade(): String { //todo make handleResult
            val query = database.child("users").child(Firebase.auth.uid!!)
            var classId = ""
            var done = false
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    classId = snapshot.getValue(ClassId::class.java)!!.classId
                    done = true
                }

                override fun onCancelled(error: DatabaseError) {
                    classId = error.message
                    done = true
                }
            })
            while (!done)
                delay(50)
            val newQuery = database.child("classes").child(classId)
            done = false
            var className = ""
            newQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    className = snapshot.getValue(ClassName::class.java)!!.name
                    done = true
                }

                override fun onCancelled(error: DatabaseError) {
                    className = error.message
                    done = true
                }
            })
            while (!done)
                delay(50)
            return className
        }
    }
}

private data class ClassId(val classId: String = "")
private data class ClassName(val name: String = "")