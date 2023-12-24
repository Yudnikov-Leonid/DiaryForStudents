package com.maxim.diaryforstudents.core

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class Core(private val context: Context) {
    private val navigation = Navigation.Base()
    fun manageResource() = ManageResource.Base(context.resources)
    fun dataBase(): DatabaseReference = Firebase.database(DATABASE_URL).reference.root
    fun navigation(): Navigation.Mutable = navigation

    companion object {
        private const val DATABASE_URL =
            "https://diary-ee752-default-rtdb.europe-west1.firebasedatabase.app"
    }
}