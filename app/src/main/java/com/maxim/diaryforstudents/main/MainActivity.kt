package com.maxim.diaryforstudents.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.ProvideColorManager
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.core.sl.ProvideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ProvideViewModel, HideKeyboard, ProvideColorManager {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.observe(this) {
            it.show(supportFragmentManager, R.id.container)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
        return (application as ProvideViewModel).viewModel(clazz)
    }

    override fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    }

    override fun colorManager(): ColorManager {
        return (application as ProvideColorManager).colorManager()
    }
}

interface HideKeyboard {
    fun hideKeyboard()
}