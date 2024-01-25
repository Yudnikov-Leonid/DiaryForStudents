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
import com.maxim.diaryforstudents.profile.data.ClientWrapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Core : ManageResource, ProvideService, ProvideMyUser, ProvideCreateLessonCache,
    ProvideSelectedClassCache, ProvideClientWrapper, ProvideOpenNewsData, ProvideNavigation,
    ProvideLessonsMapper, ProvideRetrofit {

    class Base(private val context: Context) : Core {

        private val manageResource by lazy { ManageResource.Base(context.resources) }
        private val navigation = Navigation.Base()
        private val openNewsData by lazy { OpenNewsData.Base() }

        private val lessonsMapper = LessonMapper.Base(manageResource)
        override fun lessonsMapper() = lessonsMapper

        val client: OkHttpClient.Builder

        init {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            client = OkHttpClient.Builder()
            client.addInterceptor(interceptor)
        }

        private val retrofit =
            Retrofit.Builder().baseUrl("https://mp.43edu.ru/journals/").client(client.build())
                .addConverterFactory(GsonConverterFactory.create()).build()

        override fun retrofit() = retrofit


        private val service = Service.Base(context, CoroutineHandler.Base())
        override fun service() = service

        override fun navigation(): Navigation.Mutable = navigation
        override fun openNewsData() = openNewsData

        private val selectedClassCache: SelectedClassCache.Mutable = SelectedClassCache.Base()
        override fun selectedClassCache() = selectedClassCache

        private val clientWrapper = ClientWrapper.Base(googleClient())
        override fun clientWrapper() = clientWrapper

        private val createLessonCache: CreateLessonCache.Mutable = CreateLessonCache.Base()
        override fun createLessonCache() = createLessonCache
        private val myUser = MyUser.Base(object : NavigateToLogin {
            override fun navigate() {
                navigation.update(LoginScreen)
            }
        })

        override fun myUser() = myUser
        override fun string(key: Int) = manageResource.string(key)

        private fun googleClient(): GoogleSignInClient {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(string(R.string.client_web_id))
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(context, gso)
        }
    }
}

interface ProvideService {
    fun service(): Service
}

interface ProvideMyUser {
    fun myUser(): MyUser
}

interface ProvideCreateLessonCache {
    fun createLessonCache(): CreateLessonCache.Mutable
}

interface ProvideSelectedClassCache {
    fun selectedClassCache(): SelectedClassCache.Mutable
}

interface ProvideClientWrapper {
    fun clientWrapper(): ClientWrapper
}

interface ProvideOpenNewsData {
    fun openNewsData(): OpenNewsData.Mutable
}

interface ProvideNavigation {
    fun navigation(): Navigation.Mutable
}

interface ProvideLessonsMapper {
    fun lessonsMapper(): LessonMapper
}

interface ProvideRetrofit {
    fun retrofit(): Retrofit
}