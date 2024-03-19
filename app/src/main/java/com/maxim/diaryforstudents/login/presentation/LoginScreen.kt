package com.maxim.diaryforstudents.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ProvideViewModel

object LoginScreen : Screen {
    @Composable
    override fun Show() {
        val viewModel =
            (LocalContext.current.applicationContext as ProvideViewModel).viewModel(LoginViewModel::class.java)

        var login by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        val loginMessage by viewModel.loginErrorMessage().observeAsState()
        val passwordMessage by viewModel.passwordErrorMessage().observeAsState()
        val errorMessage by viewModel.errorMessage().observeAsState()
        val isLoading by viewModel.isLoading().observeAsState()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Welcome back!")
            OutlinedTextField(
                value = login,
                onValueChange = {
                    login = it
                    viewModel.hideErrors()
                },
                label = { Text("Login") },
                isError = loginMessage?.isNotEmpty() ?: false,
                supportingText = { Text(loginMessage ?: "") },
                enabled = isLoading ?: true
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.hideErrors()
                },
                label = { Text("Password") },
                isError = passwordMessage?.isNotEmpty() ?: false,
                supportingText = { Text(passwordMessage ?: "") },
                enabled = isLoading ?: true
            )
            if (isLoading == true)
                CircularProgressIndicator(modifier = Modifier.wrapContentSize())
            Button(
                onClick = {
                    viewModel.hideErrors()
                    viewModel.login(login, password)
                },
                enabled = isLoading ?: true
            ) {
                Text(text = "Login")
            }
            if (errorMessage != null && errorMessage != "")
                Text(text = errorMessage!!, style = TextStyle(color = Color.Red, fontSize = 20.sp))
        }
    }
}