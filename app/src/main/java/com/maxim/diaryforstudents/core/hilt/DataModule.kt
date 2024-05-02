package com.maxim.diaryforstudents.core.hilt

import android.content.Context
import androidx.room.Room
import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.performance.common.room.PerformanceDao
import com.maxim.diaryforstudents.performance.common.room.PerformanceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
}