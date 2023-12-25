package com.maxim.diaryforstudents.profile.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.maxim.diaryforstudents.R
import kotlinx.coroutines.delay

interface ProfileCloudDataSource {
    fun signOut()

    suspend fun getGrade(): String

    //todo fix context in constructor
    class Base(private val context: Context, private val database: DatabaseReference) :
        ProfileCloudDataSource {
        override fun signOut() {
            Firebase.auth.signOut()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.client_web_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            googleSignInClient.signOut()
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