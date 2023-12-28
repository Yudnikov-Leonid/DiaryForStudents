package com.maxim.diaryforstudents.core

import android.app.Application
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.ProvideViewModel
import com.maxim.diaryforstudents.core.sl.ViewModelFactory

class App : Application(), ProvideViewModel {
    private lateinit var factory: ViewModelFactory
    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
        factory = ViewModelFactory.Base(Core(this))
    }

    override fun <T : ViewModel> viewModel(clazz: Class<T>) = factory.viewModel(clazz)
}