package com.maxim.diaryforstudents.core

import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.DependencyContainer
import com.maxim.diaryforstudents.core.sl.ProvideViewModel
import com.maxim.diaryforstudents.core.sl.ViewModelFactory

class App : Application(), ProvideViewModel, ProvideColorManager {
    private lateinit var factory: ViewModelFactory
    private lateinit var core: Core

    override fun onCreate() {
        super.onCreate()
        core = Core.Base(this)
        factory = ViewModelFactory.Empty
        val dependencyContainer = DependencyContainer.Base(core, object : ClearViewModel {
            override fun clearViewModel(clasz: Class<out ViewModel>) {
                factory.clearViewModel(clasz)
            }
        })
        factory = ViewModelFactory.Base(ProvideViewModel.Base(dependencyContainer))

        Log.d("MyLog", "res: ${R.color.light_green}")
        Log.d("MyLog", "resColor: ${ContextCompat.getColor(this, R.color.light_green)}")
        Log.d("MyLog", "savedValue: ${core.simpleStorage().read("color_5", 0)}")
        Log.d(
            "MyLog",
            "savedColor: ${core.simpleStorage().read("color_5", 0).toString(16)}"
        )
    }

    override fun colorManager() = core.colorManager()

    override fun <T : ViewModel> viewModel(clasz: Class<T>) = factory.viewModel(clasz)
}

interface ProvideColorManager {
    fun colorManager(): ColorManager
}