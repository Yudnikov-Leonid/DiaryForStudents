package com.maxim.diaryforstudents.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.ProvideColorManager
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.core.presentation.HideKeyboard
import com.maxim.diaryforstudents.core.sl.ProvideViewModel

class MainActivity : AppCompatActivity(), ProvideViewModel, HideKeyboard, ProvideColorManager {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = viewModel(MainViewModel::class.java)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Container(viewModel)
            }
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

@Composable
fun Container(viewModel: MainViewModel) {
    val state by viewModel.state().collectAsState()

    state.Show()
}