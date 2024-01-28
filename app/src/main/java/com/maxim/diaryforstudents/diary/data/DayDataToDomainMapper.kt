package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.diary.domain.DayDomain

class DayDataToDomainMapper: DayData.Mapper<DayDomain> {
    override fun map(date: Int, isSelected: Boolean) = DayDomain(date, isSelected)
}