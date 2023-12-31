package com.maxim.diaryforstudents.editDiary.createLesson.presentation

interface UiValidator {
    fun isValid(value: String)

    class Empty: UiValidator {
        override fun isValid(value: String) {
            if (value.isEmpty()) throw ValidationException("The field is empty")
        }
    }
}

class ValidationException(message: String): Exception(message)