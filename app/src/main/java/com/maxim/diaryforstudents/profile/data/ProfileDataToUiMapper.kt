package com.maxim.diaryforstudents.profile.data

import com.maxim.diaryforstudents.profile.presentation.ProfileUi

class ProfileDataToUiMapper: ProfileData.Mapper<ProfileUi> {
    override fun map(fullName: String, schoolName: String, grade: String) =
        ProfileUi(fullName, schoolName, grade)
}