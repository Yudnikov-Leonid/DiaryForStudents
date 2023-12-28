package com.maxim.diaryforstudents.openNews

import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Screen

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