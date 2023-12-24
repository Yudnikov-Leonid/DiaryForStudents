package com.maxim.diaryforstudents.openNews

import com.maxim.diaryforstudents.core.BaseViewModel
import com.maxim.diaryforstudents.core.ClearViewModel
import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.core.Screen

class OpenNewsViewModel(
    private val data: OpenNewsData.Read,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel
): BaseViewModel() {
    fun data() = data.read()

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(OpenNewsViewModel::class.java)
    }
}