package com.maxim.diaryforstudents.selectUser.presentation

import com.maxim.diaryforstudents.selectUser.data.SelectUserData

class SelectUserDataToUiMapper: SelectUserData.Mapper<SelectUserUi> {
    override fun map(name: String, school: String) = SelectUserUi.Base(name, school)
}