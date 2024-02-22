package com.maxim.diaryforstudents.core.sl

import android.content.Context
import androidx.room.Room
import com.maxim.diaryforstudents.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.calculateAverage.data.CalculateStorage
import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.service.CoroutineHandler
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.login.data.LoginService
import com.maxim.diaryforstudents.openNews.OpenNewsStorage
import com.maxim.diaryforstudents.performance.common.room.PerformanceDatabase
import com.maxim.diaryforstudents.performance.common.sl.MarksModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Core : ManageResource, ProvideService, ProvideOpenNewsData, ProvideNavigation,
    ProvideRetrofit, ProvideSimpleStorage, ProvideEduUser, ProvideLessonDetailsStorage,
    ProvideCalculateStorage, ProvideMarksModule, ProvideAnalyticsStorage, ProvideLoginRepository,
    ProvidePerformanceDatabase {

    class Base(private val context: Context) : Core {

        private val manageResource by lazy { ManageResource.Base(context.resources) }
        private val navigation = Navigation.Base()
        private val openNewsStorage by lazy { OpenNewsStorage.Base() }

        private val client: OkHttpClient.Builder

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

        private val simpleStorage =
            SimpleStorage.Base(context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE))

        override fun simpleStorage() = simpleStorage

        private val eduUser = EduUser.Base(simpleStorage)
        override fun eduUser() = eduUser

        private val lessonDetailsStorage = LessonDetailsStorage.Base()
        override fun lessonDetailsStorage() = lessonDetailsStorage

        private val calculateStorage = CalculateStorage.Base()
        override fun calculateStorage() = calculateStorage

        private val marksModule = MarksModule.Base(this)
        override fun marksModule() = marksModule

        private val analyticsStorage = AnalyticsStorage.Base()
        override fun analyticsStorage() = analyticsStorage

        private var loginRepository: LoginRepository? = null
        override fun loginRepository(): LoginRepository {
            if (loginRepository == null)
                loginRepository =
                    LoginRepository.Base(retrofit.create(LoginService::class.java), eduUser)
            return loginRepository!!
        }

        override fun clearLoginRepository() {
            loginRepository = null
        }

        private val database by lazy {
            Room.databaseBuilder(context, PerformanceDatabase::class.java, "performance_database").build()
        }
        override fun performanceDatabase() = database

        private val service = Service.Base(context, CoroutineHandler.Base())
        override fun service() = service

        override fun navigation(): Navigation.Mutable = navigation
        override fun openNewsData() = openNewsStorage

        override fun string(key: Int) = manageResource.string(key)
        override fun string(key: Int, format: String) = manageResource.string(key, format)

        companion object {
            private const val STORAGE_NAME = "DIARY_STORAGE"
        }
    }
}

interface ProvideService {
    fun service(): Service
}

interface ProvideOpenNewsData {
    fun openNewsData(): OpenNewsStorage.Mutable
}

interface ProvideNavigation {
    fun navigation(): Navigation.Mutable
}

interface ProvideRetrofit {
    fun retrofit(): Retrofit
}

interface ProvideSimpleStorage {
    fun simpleStorage(): SimpleStorage
}

interface ProvideEduUser {
    fun eduUser(): EduUser
}

interface ProvideLessonDetailsStorage {
    fun lessonDetailsStorage(): LessonDetailsStorage.Mutable
}

interface ProvideCalculateStorage {
    fun calculateStorage(): CalculateStorage.Mutable
}

interface ProvideMarksModule {
    fun marksModule(): MarksModule
}

interface ProvideAnalyticsStorage {
    fun analyticsStorage(): AnalyticsStorage.Mutable
}

interface ProvideLoginRepository {
    fun loginRepository(): LoginRepository
    fun clearLoginRepository()
}

interface ProvidePerformanceDatabase {
    fun performanceDatabase(): PerformanceDatabase
}