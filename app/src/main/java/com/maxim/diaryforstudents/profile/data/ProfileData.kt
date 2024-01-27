package com.maxim.diaryforstudents.profile.data

import com.maxim.diaryforstudents.profile.presentation.ProfileUi

data class ProfileData(
    private val fullName: String,
    private val schoolName: String,
    private val grade: String
) {
    fun toUi() = ProfileUi(fullName, schoolName, grade)
}