package com.maxim.diaryforstudents.core.sl

import androidx.lifecycle.ViewModel

interface ViewModelFactory : ProvideViewModel, ClearViewModel {
    class Base(
        private val provider: ProvideViewModel
    ) : ViewModelFactory {
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

    object Empty: ViewModelFactory {
        override fun <T : ViewModel> viewModel(clasz: Class<T>): T {
            throw IllegalStateException("empty viewModel factory used")
        }

        override fun clearViewModel(clasz: Class<out ViewModel>) {
            throw IllegalStateException("empty viewModel factory used")
        }
    }
}

interface ProvideViewModel {
    fun <T : ViewModel> viewModel(clasz: Class<T>): T

    class Base(private val dependencyContainer: DependencyContainer) : ProvideViewModel {
        override fun <T : ViewModel> viewModel(clasz: Class<T>): T =
            dependencyContainer.module(clasz).viewModel() as T
    }
}

interface ClearViewModel {
    fun clearViewModel(clasz: Class<out ViewModel>)
}