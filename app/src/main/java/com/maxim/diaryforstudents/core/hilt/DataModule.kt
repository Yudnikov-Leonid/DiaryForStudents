package com.maxim.diaryforstudents.core.hilt

import android.content.Context
import androidx.room.Room
import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.core.sl.ManageResource
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.data.DiaryService
import com.maxim.diaryforstudents.diary.data.room.MenuLessonsDao
import com.maxim.diaryforstudents.diary.data.room.MenuLessonsDatabase
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.HandleMarkType
import com.maxim.diaryforstudents.performance.common.data.HandleResponse
import com.maxim.diaryforstudents.performance.common.data.PerformanceCloudDataSource
import com.maxim.diaryforstudents.performance.common.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.common.data.PerformanceService
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.room.PerformanceDao
import com.maxim.diaryforstudents.performance.common.room.PerformanceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun providePerformanceDatabase(@ApplicationContext context: Context): PerformanceDatabase {
        return Room.databaseBuilder(
            context,
            PerformanceDatabase::class.java,
            "performance_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePerformanceDao(database: PerformanceDatabase): PerformanceDao {
        return database.dao()
    }

    @Provides
    @Singleton
    fun provideSimpleStorage(@ApplicationContext context: Context): SimpleStorage {
        return SimpleStorage.Base(
            context.getSharedPreferences(
                "diary_storage",
                Context.MODE_PRIVATE
            )
        )
    }

    @Provides
    @Singleton
    fun provideEduUser(simpleStorage: SimpleStorage, performanceDao: PerformanceDao): EduUser {
        return EduUser.Base(simpleStorage, performanceDao)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
        client.addInterceptor(interceptor)
        return Retrofit.Builder().baseUrl("https://mp.43edu.ru/journals/").client(client.build())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideDairyRepository(
        service: DiaryService,
        menuLessonsDao: MenuLessonsDao,
        eduUser: EduUser, manageResource: ManageResource
    ): DiaryRepository {
        return DiaryRepository.Base(
            service,
            menuLessonsDao,
            Formatter.Base,
            eduUser,
            manageResource,
            HandleMarkType.Base()
        )
    }

    @Provides
    @Singleton
    fun provideDiaryService(retrofit: Retrofit): DiaryService {
        return retrofit.create(DiaryService::class.java)
    }

    @Provides
    @Singleton
    fun provideMenuLessonsDao(@ApplicationContext context: Context): MenuLessonsDao {
        return Room.databaseBuilder(
            context,
            MenuLessonsDatabase::class.java,
            "menu_lessons_database"
        ).build().dao()
    }

    @Provides
    @Singleton
    fun providePerformanceInteractor(
        performanceRepository: PerformanceRepository,
        diaryRepository: DiaryRepository,
        manageResource: ManageResource
    ): PerformanceInteractor {
        return PerformanceInteractor.Base(
            performanceRepository,
            FailureHandler.Base(),
            diaryRepository,
            manageResource
        )
    }

    @Provides
    @Singleton
    fun providePerformanceRepository(
        performanceService: PerformanceService,
        eduUser: EduUser,
        simpleStorage: SimpleStorage,
        dao: PerformanceDao
    ): PerformanceRepository {
        return PerformanceRepository.Base(
            PerformanceCloudDataSource.Base(
                performanceService,
                eduUser
            ),
            HandleResponse.Base(),
            simpleStorage,
            HandleMarkType.Base(),
            dao
        )
    }

    @Provides
    @Singleton
    fun providePerformanceService(retrofit: Retrofit): PerformanceService {
        return retrofit.create(PerformanceService::class.java)
    }
}