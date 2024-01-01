package com.maxim.diaryforstudents.core.sl

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.data.LessonMapper
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.service.CoroutineHandler
import com.maxim.diaryforstudents.core.service.MyUser
import com.maxim.diaryforstudents.core.service.NavigateToLogin
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.editDiary.common.CreateLessonCache
import com.maxim.diaryforstudents.editDiary.common.SelectedClassCache
import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.openNews.OpenNewsData

class Core(private val context: Context) : ManageResource {

    private val manageResource by lazy { ManageResource.Base(context.resources) }
    private val navigation = Navigation.Base()
    private val openNewsData by lazy { OpenNewsData.Base() }
    private val lessonsMapper = LessonMapper.Base(manageResource)
    fun context() = context
    fun lessonsMapper() = lessonsMapper
    private val service = Service.Base(context, CoroutineHandler.Base())
    fun service() = service

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

    private val createLessonCache = CreateLessonCache.Base()
    fun createLessonCache() = createLessonCache
    private val myUser = MyUser.Base(object : NavigateToLogin {
        override fun navigate() {
            navigation.update(LoginScreen)
        }
    })

    fun myUser() = myUser

    override fun string(key: Int) = manageResource.string(key)
}