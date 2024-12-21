package com.maxim.diaryforstudents.settings.utilities

import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel

class UtilitiesViewModel(private val navigation: Navigation.Update, private val clearViewModel: ClearViewModel): ViewModel(), GoBack {
    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clearViewModel(UtilitiesViewModel::class.java)
    }
}