package com.maxim.diaryforstudents.openNews

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module

class OpenNewsModule(private val core: Core, private val clear: ClearViewModel) :
    Module<OpenNewsViewModel> {
    override fun viewModel() = OpenNewsViewModel(
        core.downloader(),
        OpenNewsCommunication.Base(),
        core.openNewsData(),
        core.navigation(),
        clear
    )
}