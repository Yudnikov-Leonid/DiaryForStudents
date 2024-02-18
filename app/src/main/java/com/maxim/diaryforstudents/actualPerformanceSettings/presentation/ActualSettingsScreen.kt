package com.maxim.diaryforstudents.actualPerformanceSettings.presentation

import androidx.fragment.app.FragmentManager
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.presentation.SerializableLambda

class ActualSettingsScreen(private val reload: SerializableLambda): Screen {
    override fun show(fragmentManager: FragmentManager, containerId: Int) {
        ActualSettingsDialogFragment.newInstance(reload).show(fragmentManager, "")
    }
}