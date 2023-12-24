package com.maxim.diaryforstudents.core

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.maxim.diaryforstudents.openNews.OpenNewsData

class Core(private val context: Context) {

    init {
        Firebase.database(DATABASE_URL).setPersistenceEnabled(false)
    }

    private val navigation = Navigation.Base()
    private val openNewsData = OpenNewsData.Base()
    fun context() = context
    fun manageResource() = ManageResource.Base(context.resources)
    fun dataBase(): DatabaseReference = Firebase.database(DATABASE_URL).reference.root
    fun navigation(): Navigation.Mutable = navigation
    fun openNewsData() = openNewsData

    companion object {
        private const val DATABASE_URL =
            "https://diary-ee752-default-rtdb.europe-west1.firebasedatabase.app"
    }
}