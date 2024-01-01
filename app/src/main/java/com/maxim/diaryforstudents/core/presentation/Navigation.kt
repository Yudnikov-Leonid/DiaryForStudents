package com.maxim.diaryforstudents.core.presentation

interface Navigation {
    interface Update : Communication.Update<Screen>
    interface Observe : Communication.Observe<Screen>
    interface Mutable : Update, Observe
    class Base : Communication.Single<Screen>(), Mutable
}