package com.maxim.diaryforstudents.fakes

import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.core.service.ServiceValueEventListener
import junit.framework.TestCase.assertEquals

class FakeService : Service {
    private val actualMap = mutableMapOf<List<String>, List<Pair<String, Any>>>()
    private var counter = 0
    fun getReturn(map: Map<List<String>, List<Pair<String, Any>>>) {
        map.forEach {
            actualMap[it.key] = it.value
        }
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, counter)
    }


    override suspend fun setValue(
        childOne: String,
        childTwo: String,
        childThree: String,
        value: Any
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun setValue(childOne: String, childTwo: String, value: Any) {
        TODO("Not yet implemented")
    }

    override suspend fun <T : Any> get(
        childOne: String,
        childTwo: String,
        clasz: Class<T>
    ): List<Pair<String, T>> {
        counter++
        actualMap.forEach {
            if (it.key[0] == childOne && it.key[1] == childTwo) return it.value as List<Pair<String, T>>
        }
        return emptyList()
    }

    override suspend fun <T : Any> getOrderByChild(
        childOne: String,
        orderByChild: String,
        equalTo: String,
        clasz: Class<T>
    ): List<Pair<String, T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T : Any> getOrderByChild(
        childOne: String,
        orderByChild: String,
        equalTo: Double,
        clasz: Class<T>
    ): List<Pair<String, T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T : Any> getOrderByKey(
        childOne: String,
        equalTo: String,
        clasz: Class<T>
    ): List<Pair<String, T>> {
        TODO("Not yet implemented")
    }

    override fun removeAsync(childOne: String, childTwo: String) {
        TODO("Not yet implemented")
    }

    override fun pushValueAsync(childOne: String, value: Any) {
        TODO("Not yet implemented")
    }

    override suspend fun pushValue(childOne: String, value: Any) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> listen(
        childOne: String,
        clasz: Class<T>,
        listener: ServiceValueEventListener<T>
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> listen(
        childOne: String,
        childTwo: String,
        clasz: Class<T>,
        listener: ServiceValueEventListener<T>
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> listenByChild(
        childOne: String,
        orderByChild: String,
        equalTo: String,
        clasz: Class<T>,
        listener: ServiceValueEventListener<T>
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> listenByChild(
        childOne: String,
        orderByChild: String,
        equalTo: Double,
        clasz: Class<T>,
        listener: ServiceValueEventListener<T>
    ) {
        TODO("Not yet implemented")
    }
}