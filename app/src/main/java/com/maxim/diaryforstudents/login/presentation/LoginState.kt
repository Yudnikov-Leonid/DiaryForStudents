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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.io.Serializable

interface LoginState : Serializable {

    @Composable
    fun Show(viewModel: LoginViewModel)

    object Initial : LoginState {
        @Composable
        override fun Show(viewModel: LoginViewModel) {
            var login by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Welcome back!")
                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Login") })
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") })
                Button(onClick = { viewModel.login(login, password) }) {
                    Text(text = "Login")
                }
            }
        }
    }

    object Loading : LoginState {

        @Composable
        override fun Show(viewModel: LoginViewModel) {
            var login by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Welcome back!")
                OutlinedTextField(
                    enabled = false,
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Login") })
                OutlinedTextField(
                    enabled = false,
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") })
                CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                Button(onClick = {}, enabled = false) {
                    Text(text = "Login")
                }
            }
        }
    }

    data class LoginError(private val message: String) : LoginState {

        @Composable
        override fun Show(viewModel: LoginViewModel) {
            var login by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Welcome back!")
                OutlinedTextField(
                    value = login,
                    isError = true,
                    supportingText = { Text(message) },
                    onValueChange = { login = it },
                    label = { Text("Login") })
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") })
                Button(onClick = { viewModel.login(login, password) }) {
                    Text(text = "Login")
                }
            }
        }
    }

    data class PasswordError(private val message: String) : LoginState {

        @Composable
        override fun Show(viewModel: LoginViewModel) {
            var login by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Welcome back!")
                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Login") })
                OutlinedTextField(
                    value = password,
                    isError = true,
                    supportingText = { Text(message) },
                    onValueChange = { password = it },
                    label = { Text("Password") })
                Button(onClick = { viewModel.login(login, password) }) {
                    Text(text = "Login")
                }
            }
        }
    }

    data class Error(private val message: String) : LoginState {

        @Composable
        override fun Show(viewModel: LoginViewModel) {

        }
    }

    object Empty : LoginState {

        @Composable
        override fun Show(viewModel: LoginViewModel) = Unit
    }
}