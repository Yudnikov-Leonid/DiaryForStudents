package com.maxim.diaryforstudents.settings.themes

import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.settings.data.SettingsThemesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SettingsThemesModule {

    @Provides
    fun provideCommunication(): SettingsThemesCommunication = SettingsThemesCommunication.Base()

    @Provides
    fun provideListOfColors(): ListOfColors {
        return ListOfColors(
            listOf(
                R.color.light_green,
                R.color.green,
                R.color.yellow,
                R.color.red,
                R.color.red
            )
        )
    }

    @Provides
    fun provideSettingsThemesRepository(colorManager: ColorManager): SettingsThemesRepository {
        return SettingsThemesRepository.Base(colorManager)
    }
}