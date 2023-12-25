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

        override suspend fun getGrade(): String {
            val query = database.child("users").child(Firebase.auth.uid!!)
            var grade = ""
            var done = false
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    grade = snapshot.getValue(Grade::class.java)!!.grade
                    done = true
                }

                override fun onCancelled(error: DatabaseError) {
                    grade = error.message
                    done = true
                }
            })
            while (!done)
                delay(50)
            return grade
        }
    }
}

private data class Grade(val grade: String = "")