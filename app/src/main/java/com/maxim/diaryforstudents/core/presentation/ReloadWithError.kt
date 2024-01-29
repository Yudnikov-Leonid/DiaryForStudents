package com.maxim.diaryforstudents.core.presentation

interface Reload {
    fun reload()
}

interface ReloadWithError: Reload {
    fun error(message: String)
}