package com.maxim.diaryforstudents.news.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface NewsCommunication: Communication.All<NewsState> {
    class Base : Communication.RegularWithDeath<NewsState>(), NewsCommunication
}