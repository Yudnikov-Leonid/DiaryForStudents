package com.maxim.diaryforstudents.settings

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.settings.data.SettingsThemesRepository
import com.maxim.diaryforstudents.settings.themes.OpenColorPicker
import com.maxim.diaryforstudents.settings.themes.SettingsThemesCommunication
import com.maxim.diaryforstudents.settings.themes.SettingsThemesState
import com.maxim.diaryforstudents.settings.themes.SettingsThemesViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class SettingsThemesViewModelTest {
    private lateinit var viewModel: SettingsThemesViewModel
    private lateinit var communication: FakeSettingsThemesCommunication
    private lateinit var repository: FakeSettingsThemesRepository
    private lateinit var order: Order
    private lateinit var navigation: FakeNavigation
    private lateinit var clearViewModel: FakeClearViewModel
    private lateinit var colorsList: List<Int>

    @Before
    fun setUp() {
        communication = FakeSettingsThemesCommunication()
        repository = FakeSettingsThemesRepository()
        order = Order()
        navigation = FakeNavigation(order)
        clearViewModel = FakeClearViewModel(order)
        colorsList = listOf(1, 2, 3, 4, 5)
        viewModel = SettingsThemesViewModel(communication, repository, colorsList, navigation, clearViewModel)
    }

    @Test
    fun test_reload() {
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            SettingsThemesState.Base(
                colorsList[0],
                colorsList[1],
                colorsList[2],
                colorsList[3],
                colorsList[4],
            )
        )
    }

    @Test
    fun test_save_color() {
        viewModel.saveColor(1234, "key")
        repository.checkSaveColorCalledTimes(1)
        repository.checkSaveColorCalledWith(1234, "key")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            SettingsThemesState.Base(
                colorsList[0],
                colorsList[1],
                colorsList[2],
                colorsList[3],
                colorsList[4],
            )
        )
    }

    @Test
    fun test_reset_color() {
        viewModel.resetColor("key")
        repository.checkResetCalledTimes(1)
        repository.checkResetCalledWith("key")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            SettingsThemesState.Base(
                colorsList[0],
                colorsList[1],
                colorsList[2],
                colorsList[3],
                colorsList[4],
            )
        )
    }

    @Test
    fun test_go_back() {
        viewModel.goBack()
        navigation.checkCalledWith(Screen.Pop)
        clearViewModel.checkCalledWith(SettingsThemesViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_open_color_picker_has_color() {
        val fakeOpenPicker = FakeOpenPicker()
        repository.hasColorMustReturn(true)
        repository.defaultColorMustReturn(999)
        viewModel.openColorPicker("key", 1234, fakeOpenPicker)
        repository.checkHasColorCalledTimes(1)
        repository.checkHasColorCalledWith("key")
        repository.checkDefaultColorCalledTimes(1)
        repository.checkDefaultColorCalledWith("key")
        fakeOpenPicker.checkOpenCalledTimes(1)
        fakeOpenPicker.checkOpenCalledWith(999, false, "key")
    }

    @Test
    fun test_open_color_picker_has_not_color() {
        val fakeOpenPicker = FakeOpenPicker()
        repository.hasColorMustReturn(false)
        viewModel.openColorPicker("key", 1234, fakeOpenPicker)
        repository.checkHasColorCalledTimes(1)
        repository.checkHasColorCalledWith("key")
        repository.checkDefaultColorCalledTimes(0)
        fakeOpenPicker.checkOpenCalledTimes(1)
        fakeOpenPicker.checkOpenCalledWith(1234, true, "key")
    }
}

private class FakeSettingsThemesCommunication: SettingsThemesCommunication {
    private val list = mutableListOf<SettingsThemesState>()

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: SettingsThemesState) {
        assertEquals(expected, list.last())
    }

    override fun update(value: SettingsThemesState) {
        list.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<SettingsThemesState>) {
        throw IllegalStateException("not using in tests")
    }
}

private class FakeSettingsThemesRepository: SettingsThemesRepository {
    private val saveColorList = mutableListOf<Pair<Int, String>>()

    fun checkSaveColorCalledTimes(expected: Int) {
        assertEquals(expected, saveColorList.size)
    }

    fun checkSaveColorCalledWith(expectedColor: Int, expectedKey: String) {
        assertEquals(Pair(expectedColor, expectedKey), saveColorList.last())
    }

    override fun saveColor(color: Int, key: String) {
        saveColorList.add(Pair(color, key))
    }

    private val defaultColorList = mutableListOf<Pair<String, Int>>()
    private var defaultColorValue = 0

    fun defaultColorMustReturn(value: Int) {
        defaultColorValue = value
    }

    fun checkDefaultColorCalledTimes(expected: Int) {
        assertEquals(expected, defaultColorList.size)
    }

    fun checkDefaultColorCalledWith(expected: String) {
        assertEquals(expected, defaultColorList.last().first)
    }

    override fun defaultColor(key: String, default: Int): Int {
        defaultColorList.add(Pair(key, default))
        return defaultColorValue
    }

    private var hasColorValue = true
    private val hasColorList = mutableListOf<String>()
    fun hasColorMustReturn(value: Boolean) {
        hasColorValue = value
    }

    fun checkHasColorCalledTimes(expected: Int) {
        assertEquals(expected, hasColorList.size)
    }

    fun checkHasColorCalledWith(expected: String) {
        assertEquals(expected, hasColorList.last())
    }

    override fun hasColor(key: String): Boolean {
        hasColorList.add(key)
        return hasColorValue
    }

    private val resetColorList = mutableListOf<String>()

    fun checkResetCalledTimes(expected: Int) {
        assertEquals(expected, resetColorList.size)
    }

    fun checkResetCalledWith(expected: String) {
        assertEquals(expected, resetColorList.last())
    }

    override fun resetColor(key: String) {
        resetColorList.add(key)
    }
}

private class FakeOpenPicker : OpenColorPicker {
    private val list = mutableListOf<List<Any>>()

    fun checkOpenCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkOpenCalledWith(
        expectedDefaultColor: Int,
        expectedParseColor: Boolean,
        expectedKey: String
    ) {
        assertEquals(listOf(expectedDefaultColor, expectedParseColor, expectedKey), list.last())
    }

    override fun open(defaultColor: Int, parseColor: Boolean, key: String) {
        list.add(listOf(defaultColor, parseColor, key))
    }
}