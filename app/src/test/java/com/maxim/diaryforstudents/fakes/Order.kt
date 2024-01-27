package com.maxim.diaryforstudents.fakes

import junit.framework.TestCase.assertEquals

class Order {
    private val list = mutableListOf<String>()

    fun add(value: String) {
        list.add(value)
    }

    fun check(expected: List<String>) {
        assertEquals(expected, list)
    }
}

const val CLEAR = "CLEAR"
const val NAVIGATION = "NAVIGATION"
const val COMMUNICATION = "COMMUNICATION"
const val REPOSITORY = "REPOSITORY"
const val OPEN_NEWS_DATA = "OPEN_NEWS_DATA"
const val STORAGE = "STORAGE"