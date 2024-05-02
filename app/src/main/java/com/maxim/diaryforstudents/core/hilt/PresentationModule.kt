package com.maxim.diaryforstudents.core.hilt

import android.content.Context
import com.maxim.diaryforstudents.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.calculateAverage.data.CalculateStorage
import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.service.CoroutineHandler
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.core.sl.ManageResource
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.diary.domain.DiaryInteractor
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.settings.data.LessonsInMenuSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    @Singleton
    fun provideNavigation(): Navigation.Mutable {
        return Navigation.Base()
    }

    @Provides
    @Singleton
    fun provideNavigationUpdate(): Navigation.Update = provideNavigation()

    @Provides
    @Singleton
    fun provideNavigationObserve(): Navigation.Observe = provideNavigation()

    @Provides
    @Singleton
    fun provideRunAsync(): RunAsync = RunAsync.Base()

    @Provides
    @Singleton
    fun provideManageResource(@ApplicationContext context: Context): ManageResource {
        return ManageResource.Base(context.resources)
    }

    @Provides
    @Singleton
    fun provideAnalyticsStorage(): AnalyticsStorage.Mutable {
        return AnalyticsStorage.Base()
    }

    @Provides
    @Singleton
    fun provideDiaryInteractor(
        repository: DiaryRepository,
        manageResource: ManageResource
    ): DiaryInteractor {
        return DiaryInteractor.Base(repository, FailureHandler.Base(), manageResource)
    }

    @Provides
    @Singleton
    fun provideAnalyticsStorageRead(): AnalyticsStorage.Read = provideAnalyticsStorage()

    @Provides
    @Singleton
    fun provideAnalyticsStorageSave(): AnalyticsStorage.Save = provideAnalyticsStorage()

    @Provides
    @Singleton
    fun provideColorManager(simpleStorage: SimpleStorage): ColorManager {
        return ColorManager.Base(simpleStorage)
    }

    @Provides
    @Singleton
    fun provideCalculateStorage(): CalculateStorage.Mutable {
        return CalculateStorage.Base()
    }

    @Provides
    @Singleton
    fun provideCalculateStorageRead(): CalculateStorage.Read = provideCalculateStorage()

    @Provides
    @Singleton
    fun provideCalculateStorageSave(): CalculateStorage.Save = provideCalculateStorage()

    @Provides
    @Singleton
    fun provideLessonDetailsStorage(): LessonDetailsStorage.Mutable {
        return LessonDetailsStorage.Base()
    }

    @Provides
    @Singleton
    fun provideLessonDetailsStorageSave(): LessonDetailsStorage.Save = provideLessonDetailsStorage()

    @Provides
    @Singleton
    fun provideLessonDetailsStorageRead(): LessonDetailsStorage.Read = provideLessonDetailsStorage()

    @Provides
    @Singleton
    fun provideService(@ApplicationContext context: Context): Service {
        return Service.Base(context, CoroutineHandler.Base())
    }

    @Provides
    @Singleton
    fun provideLessonsInMenuSettings(simpleStorage: SimpleStorage): LessonsInMenuSettings.Mutable {
        return LessonsInMenuSettings.Base(simpleStorage)
    }

    @Provides
    @Singleton
    fun provideLessonsInMenuSettingsRead(simpleStorage: SimpleStorage): LessonsInMenuSettings.Read =
        provideLessonsInMenuSettings(simpleStorage)

    @Provides
    @Singleton
    fun provideLessonsInMenuSettingsSave(simpleStorage: SimpleStorage): LessonsInMenuSettings.Save =
        provideLessonsInMenuSettings(simpleStorage)

    @Provides
    @Singleton
    fun provideDiaryDomainToUiMapper(): DiaryDomain.Mapper<DiaryUi> {
        return DiaryDomainToUiMapper(PerformanceDomainToUiMapper())
    }
}