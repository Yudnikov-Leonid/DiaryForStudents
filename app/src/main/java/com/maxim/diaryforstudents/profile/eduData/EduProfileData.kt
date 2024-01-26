package com.maxim.diaryforstudents.profile.eduData

import com.maxim.diaryforstudents.profile.presentation.EduProfileUi

data class EduProfileData(
    private val fullName: String,
    private val schoolName: String,
    private val grade: String
) {
    fun toUi() = EduProfileUi(fullName, schoolName, grade)
}