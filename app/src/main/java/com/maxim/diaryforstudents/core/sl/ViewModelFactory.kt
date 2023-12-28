package com.maxim.diaryforstudents.core.sl

import androidx.lifecycle.ViewModel

interface ViewModelFactory : ProvideViewModel, ClearViewModel {
    class Base(
        private val core: Core
    ) : ViewModelFactory { //todo move provider in constructor
        private val provider = ProvideViewModel.Base(DependencyContainer.Base(core, this))
        private val map = mutableMapOf<Class<out ViewModel>, ViewModel>()
        override fun <T : ViewModel> viewModel(clasz: Class<T>): T {
            if (map[clasz] == null)
                map[clasz] = provider.viewModel(clasz)
            return map[clasz] as T
        }

        override fun clearViewModel(clasz: Class<out ViewModel>) {
            map.remove(clasz)
        }
    }
}

interface ProvideViewModel {
    fun <T : ViewModel> viewModel(clazz: Class<T>): T

    class Base(private val dependencyContainer: DependencyContainer) : ProvideViewModel {
        override fun <T : ViewModel> viewModel(clasz: Class<T>): T =
            dependencyContainer.module(clasz).viewModel() as T
    }
}

interface ClearViewModel {
    fun clearViewModel(clasz: Class<out ViewModel>)
}