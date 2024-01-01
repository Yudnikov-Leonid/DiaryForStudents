package com.maxim.diaryforstudents.core

import android.app.Application
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.DependencyContainer
import com.maxim.diaryforstudents.core.sl.ProvideViewModel
import com.maxim.diaryforstudents.core.sl.ViewModelFactory

class App : Application(), ProvideViewModel {
    private lateinit var factory: ViewModelFactory
    override fun onCreate() {
        super.onCreate()
        factory = ViewModelFactory.Empty
        val dependencyContainer = DependencyContainer.Base(Core(this), object : ClearViewModel {
            override fun clearViewModel(clasz: Class<out ViewModel>) {
                factory.clearViewModel(clasz)
            }
        })
        factory = ViewModelFactory.Base(ProvideViewModel.Base(dependencyContainer))
    }

    override fun <T : ViewModel> viewModel(clasz: Class<T>) = factory.viewModel(clasz)
}