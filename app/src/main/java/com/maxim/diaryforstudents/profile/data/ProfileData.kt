package com.maxim.diaryforstudents.profile.data

data class ProfileData(
    private val fullName: String,
    private val schoolName: String,
    private val grade: String
) {
    interface Mapper<T> {
        fun map(fullName: String, schoolName: String, grade: String): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(fullName, schoolName, grade)
}