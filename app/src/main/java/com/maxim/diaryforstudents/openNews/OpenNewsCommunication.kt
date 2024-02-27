package com.maxim.diaryforstudents.openNews

import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.news.presentation.NewsUi

interface OpenNewsCommunication: Communication.Mutable<NewsUi> {
    class Base: Communication.Regular<NewsUi>(), OpenNewsCommunication
}