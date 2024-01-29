package com.maxim.diaryforstudents.core.sl

import android.content.Context
import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.SaveActualSettingsCommunication
import com.maxim.diaryforstudents.calculateAverage.data.CalculateStorage
import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.service.CoroutineHandler
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.openNews.OpenNewsStorage
import com.maxim.diaryforstudents.performance.common.data.DiaryService
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.PerformanceCloudDataSource
import com.maxim.diaryforstudents.performance.common.data.PerformanceDataToDomainMapper
import com.maxim.diaryforstudents.performance.common.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Core : ManageResource, ProvideService, ProvideOpenNewsData, ProvideNavigation,
    ProvideRetrofit, ProvideSimpleStorage, ProvideEduUser, ProvideLessonDetailsStorage,
    ProvideCalculateStorage, ProvideActualSettingsCommunication, ProvideMarksInteractor {

    class Base(private val context: Context) : Core {

        private val manageResource by lazy { ManageResource.Base(context.resources) }
        private val navigation = Navigation.Base()
        private val openNewsStorage by lazy { OpenNewsStorage.Base() }

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

        private val simpleStorage =
            SimpleStorage.Base(context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE))

        override fun simpleStorage() = simpleStorage

        private val eduUser = EduUser.Base(simpleStorage)
        override fun eduUser() = eduUser

        private val lessonDetailsStorage = LessonDetailsStorage.Base()
        override fun lessonDetailsStorage() = lessonDetailsStorage

        private val calculateStorage = CalculateStorage.Base()
        override fun calculateStorage() = calculateStorage

        private val actualSettingsCommunication = SaveActualSettingsCommunication.Base()
        override fun actualSettingsCommunication() = actualSettingsCommunication

        private val marksInteractor = PerformanceInteractor.Base(
            PerformanceRepository.Base(
                PerformanceCloudDataSource.Base(
                    retrofit().create(DiaryService::class.java),
                    eduUser()
                )
            ),
            simpleStorage(), FailureHandler.Base(), PerformanceDataToDomainMapper()
        )
        override fun marksInteractor() = marksInteractor

        private val service = Service.Base(context, CoroutineHandler.Base())
        override fun service() = service

        override fun navigation(): Navigation.Mutable = navigation
        override fun openNewsData() = openNewsStorage

        override fun string(key: Int) = manageResource.string(key)

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

interface ProvideActualSettingsCommunication {
    fun actualSettingsCommunication(): SaveActualSettingsCommunication.Mutable
}

//todo
interface ProvideMarksInteractor {
    fun marksInteractor(): PerformanceInteractor
}