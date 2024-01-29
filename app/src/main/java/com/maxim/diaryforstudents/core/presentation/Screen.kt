package com.maxim.diaryforstudents.core.presentation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface Screen {
    fun show(fragmentManager: FragmentManager, containerId: Int)
    abstract class Replace(private val fragmentClass: Class<out Fragment>) : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction()
                .replace(containerId, fragmentClass.getDeclaredConstructor().newInstance()).commit()
        }
    }

    abstract class Add(private val fragmentClass: Class<out Fragment>) : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction()
                .add(containerId, fragmentClass.getDeclaredConstructor().newInstance())
                .addToBackStack("")
                .commit()
        }
    }

    abstract class Dialog(private val fragmentClass: Class<out DialogFragment>) : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentClass.getDeclaredConstructor().newInstance().show(fragmentManager, "")
        }
    }

    abstract class BottomSheetFragment(private val fragmentClass: Class<out BottomSheetDialogFragment>) : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentClass.getDeclaredConstructor().newInstance().show(fragmentManager, "")
        }
    }

    object Pop : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.popBackStack()
        }
    }
}