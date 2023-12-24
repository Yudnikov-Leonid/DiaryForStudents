package com.maxim.diaryforstudents.login.presentation

import com.maxim.diaryforstudents.core.Communication

interface LoginCommunication {
    interface Update : Communication.Update<LoginState>
    interface Observe : Communication.Observe<LoginState>
    interface Mutable : Update, Observe
    class Base : Communication.Abstract<LoginState>(), Mutable
}