package com.maxim.diaryforstudents.news.presentation

import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface NewsCommunication: Communication.Mutable<NewsState> {
    class Base : Communication.Regular<NewsState>(MutableStateFlow(NewsState.Empty)), NewsCommunication
}