package com.maxim.diaryforstudents.selectUser.data

interface SelectUserData {
    interface Mapper<T> {
        fun map(name: String, school: String): T
    }

    fun <T: Any> map(mapper: Mapper<T>): T

    class Base(private val name: String, private val school: String): SelectUserData {
        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.map(name, school)
    }
}