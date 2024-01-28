package com.maxim.diaryforstudents.diary.domain

import com.maxim.diaryforstudents.diary.presentation.DayUi

class DayDomainToUiMapper: DayDomain.Mapper<DayUi> {
    override fun map(date: Int, isSelected: Boolean) = DayUi(date, isSelected)
}