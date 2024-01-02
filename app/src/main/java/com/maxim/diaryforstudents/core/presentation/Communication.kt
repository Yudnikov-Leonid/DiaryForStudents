package com.maxim.diaryforstudents.core.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.io.Serializable

interface Communication {
    interface Update<T> {
        fun update(value: T)
    }

    interface Observe<T> {
        fun observe(owner: LifecycleOwner, observer: Observer<T>)
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
        protected val liveData: MutableLiveData<T> = MutableLiveData()
    ) : Mutable<T> {
        override fun update(value: T) {
            liveData.value = value
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }
    }

    abstract class AbstractDeath<T : Serializable>(
        liveDataInner: MutableLiveData<T> = MutableLiveData()
    ) : Abstract<T>(liveDataInner), All<T> {
        override fun save(key: String, bundleWrapper: BundleWrapper.Save) {
            liveData.value?.let {
                bundleWrapper.save(key, it)
            }
        }

        override fun restore(key: String, bundleWrapper: BundleWrapper.Restore) {
            liveData.value = bundleWrapper.restore(key)
        }
    }

    abstract class Regular<T> : Abstract<T>()
    abstract class Single<T> : Abstract<T>(SingleLiveEvent())

    abstract class RegularWithDeath<T : Serializable> : AbstractDeath<T>()
    abstract class SingleWithDeath<T : Serializable> : AbstractDeath<T>(SingleLiveEvent())
}