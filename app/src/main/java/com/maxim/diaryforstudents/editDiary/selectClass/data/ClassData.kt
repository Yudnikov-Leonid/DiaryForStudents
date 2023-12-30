package com.maxim.diaryforstudents.editDiary.selectClass.data

import com.maxim.diaryforstudents.editDiary.selectClass.presentation.ClassUi

interface ClassData {
    fun mapToUi(): ClassUi
    data class Base(private val id: String, private val name: String) : ClassData {
        override fun mapToUi() = ClassUi.Base(id, name)
    }

    object Empty: ClassData {
        override fun mapToUi() = ClassUi.Empty
    }
}
