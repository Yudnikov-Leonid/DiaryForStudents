package com.maxim.diaryforstudents.core

interface Navigation {
    interface Update : Communication.Update<Screen>
    interface Observe : Communication.Observe<Screen>
    interface Mutable : Update, Observe
    class Base : Communication.Abstract<Screen>(SingleLiveEvent()), Mutable
}