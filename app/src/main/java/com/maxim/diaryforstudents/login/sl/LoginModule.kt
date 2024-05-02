package com.maxim.diaryforstudents.login.sl

import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.login.data.LoginService
import com.maxim.diaryforstudents.login.presentation.LoginCommunication
import com.maxim.diaryforstudents.login.presentation.UiValidator
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import javax.inject.Named

@dagger.Module
@InstallIn(ViewModelComponent::class)
class LoginModule {

    @Provides
    @Named("login")
    fun provideLoginValidator(): UiValidator = UiValidator.Login()

    @Provides
    @Named("password")
    fun providePasswordValidator(): UiValidator = UiValidator.Password(minLength = 3)

    @Provides
    fun provideCommunication(): LoginCommunication = LoginCommunication.Base()

    @Provides
    fun provideLoginRepository(loginService: LoginService, eduUser: EduUser): LoginRepository {
        return LoginRepository.Base(loginService, eduUser)
    }

    @Provides
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }
}