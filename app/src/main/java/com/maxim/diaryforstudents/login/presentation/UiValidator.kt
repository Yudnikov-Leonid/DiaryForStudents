package com.maxim.diaryforstudents.login.presentation

import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.sl.ManageResource

interface UiValidator {
    fun isValid(value: String, manageResource: ManageResource)

    class Empty : UiValidator {
        override fun isValid(value: String, manageResource: ManageResource) {
            if (value.isEmpty()) throw ValidationException(manageResource.string(R.string.empty_field))
        }
    }

    class Login : UiValidator {
        override fun isValid(value: String, manageResource: ManageResource) {
            if (value.isEmpty()) throw LoginException(manageResource.string(R.string.empty_field))
            val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(value)
                .matches() || (value.length == 14 && value.replace("-", "").replace(" ", "")
                .filter { it.isDigit() }.length == 11)
            if (!isValid) throw LoginException(manageResource.string(R.string.invalid_login))
        }
    }

    class Password(
        private val minLength: Int
    ) : UiValidator {
        override fun isValid(value: String, manageResource: ManageResource) {
            if (value.length < minLength)
                throw PasswordException(
                    "${
                        manageResource.string(
                            R.string.password_must_be_at_least,
                            minLength.toString()
                        )
                    } $minLength"
                )
        }
    }
}

class ValidationException(message: String) : Exception(message)
class LoginException(message: String) : Exception(message)
class PasswordException(message: String) : Exception(message)