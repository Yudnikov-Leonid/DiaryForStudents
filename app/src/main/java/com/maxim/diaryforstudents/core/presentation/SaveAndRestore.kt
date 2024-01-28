package com.maxim.diaryforstudents.core.presentation

interface SaveAndRestore {
    fun save(bundleWrapper: BundleWrapper.Save)
    fun restore(bundleWrapper: BundleWrapper.Restore)
}