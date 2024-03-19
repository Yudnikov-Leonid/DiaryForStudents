package com.maxim.diaryforstudents.openNews

import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.news.presentation.NewsUi
import kotlinx.coroutines.flow.MutableStateFlow

interface OpenNewsCommunication: Communication.Mutable<NewsUi> {
    class Base: Communication.Regular<NewsUi>(MutableStateFlow(NewsUi.Empty)), OpenNewsCommunication
}