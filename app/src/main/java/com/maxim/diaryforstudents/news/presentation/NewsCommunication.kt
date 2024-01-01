package com.maxim.diaryforstudents.news.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface NewsCommunication {
    interface Update : Communication.Update<NewsState>
    interface Observe : Communication.Observe<NewsState>
    interface Mutable : Update, Observe
    class Base : Communication.Regular<NewsState>(), Mutable
}