package com.maxim.diaryforstudents.login.presentation

interface UiValidator {
    fun isValid(value: String)

    class Empty : UiValidator {
        override fun isValid(value: String) {
            if (value.isEmpty()) throw ValidationException("The field is empty")
        }
    }

    class Login: UiValidator {
        override fun isValid(value: String) {
            if (value.isEmpty()) throw LoginException("The field is empty")
            val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
            if (!isValid) throw LoginException("Invalid login")
        }
    }

    class Password(
        private val minLength: Int
    ): UiValidator {
        override fun isValid(value: String) {
            if (value.length < minLength)
                throw PasswordException("Password length should be at least $minLength")
        }
    }
}

class ValidationException(message: String) : Exception(message)
class LoginException(message: String) : Exception(message)
class PasswordException(message: String) : Exception(message)