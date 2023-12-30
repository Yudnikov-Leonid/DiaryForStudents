package com.maxim.diaryforstudents.core.sl

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.data.LessonMapper
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.editDiary.common.SelectedClassCache
import com.maxim.diaryforstudents.openNews.OpenNewsData

class Core(private val context: Context) : ManageResource {

    init {
        Firebase.database(DATABASE_URL).setPersistenceEnabled(false)
    }

    private val manageResource by lazy { ManageResource.Base(context.resources) }
    private val navigation = Navigation.Base()
    private val openNewsData by lazy { OpenNewsData.Base() }
    private val lessonsMapper = LessonMapper.Base(manageResource)
    fun context() = context
    fun lessonsMapper() = lessonsMapper
    fun dataBase(): DatabaseReference = Firebase.database(DATABASE_URL).reference.root
    fun navigation(): Navigation.Mutable = navigation
    fun openNewsData() = openNewsData
    fun googleClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(string(R.string.client_web_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    private val selectedClassCache = SelectedClassCache.Base()
    fun selectedClassCache() = selectedClassCache

    override fun string(key: Int) = manageResource.string(key)

    companion object {
        private const val DATABASE_URL =
            "https://diary-ee752-default-rtdb.europe-west1.firebasedatabase.app"
    }
}