package com.maxim.diaryforstudents.core.presentation

import kotlinx.coroutines.flow.MutableStateFlow

interface Navigation {
    interface Update : Communication.Update<Screen>
    interface Observe : Communication.Observe<Screen>
    interface Mutable : Update, Observe
    class Base : Communication.Single<Screen>(MutableStateFlow(Screen.Empty)), Mutable
}