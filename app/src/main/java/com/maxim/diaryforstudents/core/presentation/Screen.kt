package com.maxim.diaryforstudents.core.presentation

import androidx.compose.runtime.Composable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface Screen {

    @Composable
    fun Show()

    object Empty: Screen {
        @Composable
        override fun Show() = Unit
    }
}