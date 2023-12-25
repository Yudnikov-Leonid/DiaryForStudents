package com.maxim.diaryforstudents.news.presentation

import com.maxim.diaryforstudents.core.Communication

interface NewsCommunication {
    interface Update: Communication.Update<NewsState>
    interface Observe: Communication.Observe<NewsState>
    interface Mutable: Update, Observe
    class Base: Communication.Abstract<NewsState>(), Mutable
}