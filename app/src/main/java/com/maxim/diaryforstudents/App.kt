package com.maxim.diaryforstudents

import android.app.Application
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.maxim.diaryforstudents.core.Core
import com.maxim.diaryforstudents.core.ProvideViewModel
import com.maxim.diaryforstudents.core.ViewModelFactory

class App : Application(), ProvideViewModel {
    private lateinit var factory: ViewModelFactory
    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
        factory = ViewModelFactory.Base(Core(this))
    }

    override fun <T : ViewModel> viewModel(clazz: Class<T>) = factory.viewModel(clazz)
}