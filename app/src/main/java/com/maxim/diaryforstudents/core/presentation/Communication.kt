package com.maxim.diaryforstudents.core.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable

interface Communication {
    interface Update<T> {
        fun update(value: T)
    }

    interface Observe<T> {
        fun state(): StateFlow<T>
    }

    interface Save {
        fun save(key: String, bundleWrapper: BundleWrapper.Save)
    }

    interface Restore {
        fun restore(key: String, bundleWrapper: BundleWrapper.Restore)
    }

    interface Mutable<T> : Update<T>, Observe<T>

    interface All<T : Serializable> : Mutable<T>, Save, Restore

    abstract class Abstract<T>(
        protected val state: MutableStateFlow<T>
    ) : Mutable<T> {
        override fun update(value: T) {
            state.value = value
        }

        override fun state() = state
    }

    abstract class Regular<T>(state: MutableStateFlow<T>) : Abstract<T>(state)
    abstract class Single<T>(state: MutableStateFlow<T>) : Abstract<T>(state)
}