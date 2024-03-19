package com.maxim.diaryforstudents.menu.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.maxim.diaryforstudents.core.presentation.Screen

object MenuScreen : Screen {
    @Composable
    override fun Show() {
        Text(text = "Menu text")
    }
}