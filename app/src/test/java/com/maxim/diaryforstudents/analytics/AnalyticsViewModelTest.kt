package com.maxim.diaryforstudents.analytics

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsCommunication
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsState
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsUi
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.fakes.STORAGE
import com.maxim.diaryforstudents.performance.FakePerformanceInteractor
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AnalyticsViewModelTest {
    private lateinit var viewModel: AnalyticsViewModel
    private lateinit var interactor: FakePerformanceInteractor
    private lateinit var storage: FakeAnalyticsStorage
    private lateinit var communication: FakeAnalyticsCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var clearViewModel: FakeClearViewModel
    private lateinit var runAsync: FakeRunAsync
    private lateinit var order: Order

    @Before
    fun setUp() {
        order = Order()
        interactor = FakePerformanceInteractor()
        storage = FakeAnalyticsStorage(order)
        communication = FakeAnalyticsCommunication()
        navigation = FakeNavigation(order)
        clearViewModel = FakeClearViewModel(order)
        runAsync = FakeRunAsync()
        viewModel = AnalyticsViewModel(
            interactor,
            storage,
            communication,
            navigation,
            clearViewModel,
            runAsync
        )
    }

    @Test
    fun test_reload() {
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(AnalyticsState.Loading)
        interactor.checkAnalyticsCalledTimes(1)
        interactor.checkAnalyticsCalledWith(1, "", 1, true)

        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            AnalyticsState.Base(
                listOf(
                    AnalyticsUi.Title(""),
                    AnalyticsUi.LineCommon(listOf(5.0f, 4.5f, 4.4f), listOf("1", "2", "3"), 1, 1)
                )
            )
        )
    }

    @Test
    fun test_change_interval() {
        viewModel.changeInterval(3)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(AnalyticsState.Loading)
        interactor.checkAnalyticsCalledTimes(1)
        interactor.checkAnalyticsCalledWith(1, "", 3, true)

        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            AnalyticsState.Base(
                listOf(
                    AnalyticsUi.Title(""),
                    AnalyticsUi.LineCommon(listOf(5.0f, 4.5f, 4.4f), listOf("1", "2", "3"), 1, 3)
                )
            )
        )
    }

    @Test
    fun test_change_quarter() {
        viewModel.changeQuarter(3)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(AnalyticsState.Loading)
        interactor.checkAnalyticsCalledTimes(1)
        interactor.checkAnalyticsCalledWith(3, "", 1, true)

        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            AnalyticsState.Base(
                listOf(
                    AnalyticsUi.Title(""),
                    AnalyticsUi.LineCommon(listOf(5.0f, 4.5f, 4.4f), listOf("1", "2", "3"), 3, 1)
                )
            )
        )
    }

    @Test
    fun test_init_empty_quarter() = runBlocking {
        interactor.changeQuarter(2)
        storage.readMustReturn("some lesson name", -1)
        viewModel.init(true)

        communication.checkCalledTimes(1)
        communication.checkCalledWith(AnalyticsState.Loading)
        interactor.checkAnalyticsCalledTimes(1)
        interactor.checkAnalyticsCalledWith(2, "some lesson name", 1, false)

        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            AnalyticsState.Base(
                listOf(
                    AnalyticsUi.Title("some lesson name"),
                    AnalyticsUi.LineCommon(listOf(5.0f, 4.5f, 4.4f), listOf("1", "2", "3"), 2, 1)
                )
            )
        )

        viewModel.init(false)
        communication.checkCalledTimes(2)
    }

    @Test
    fun test_init_not_empty_quarter() = runBlocking {
        interactor.changeQuarter(2)
        storage.readMustReturn("some lesson name", 4)
        viewModel.init(true)

        communication.checkCalledTimes(1)
        communication.checkCalledWith(AnalyticsState.Loading)
        interactor.checkAnalyticsCalledTimes(1)
        interactor.checkAnalyticsCalledWith(4, "some lesson name", 1, false)

        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            AnalyticsState.Base(
                listOf(
                    AnalyticsUi.Title("some lesson name"),
                    AnalyticsUi.LineCommon(listOf(5.0f, 4.5f, 4.4f), listOf("1", "2", "3"), 4, 1)
                )
            )
        )

        viewModel.init(false)
        communication.checkCalledTimes(2)
    }

    @Test
    fun test_go_back() {
        viewModel.goBack()
        navigation.checkCalledWith(Screen.Pop)
        clearViewModel.checkCalledWith(AnalyticsViewModel::class.java)
        order.check(listOf(STORAGE, NAVIGATION, CLEAR))
    }

    @Test
    fun test_save_and_restore() {
        val bundleWrapper = FakeBundleWrapper()
        viewModel.save(bundleWrapper)
        storage.checkSaveCalledTimes(1)
        communication.checkSaveCalledTimes(1)

        viewModel.restore(bundleWrapper)
        storage.checkRestoreCalledTimes(1)
        communication.checkRestoreCalledTimes(1)
    }
}

private class FakeAnalyticsStorage(private val order: Order) : AnalyticsStorage.Read {
    private var readValue = Pair("", -1)

    fun readMustReturn(str: String, int: Int) {
        readValue = Pair(str, int)
    }

    override fun read() = readValue

    override fun clear() {
        order.add(STORAGE)
    }

    private var bundleWrapper: BundleWrapper.Mutable? = null
    private var saveCounter = 0
    private var restoreCounter = 0

    override fun save(bundleWrapper: BundleWrapper.Save) {
        this.bundleWrapper = bundleWrapper as BundleWrapper.Mutable
        saveCounter++
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        assertEquals(this.bundleWrapper, bundleWrapper)
        restoreCounter++
    }

    fun checkSaveCalledTimes(expected: Int) {
        assertEquals(expected, saveCounter)
    }

    fun checkRestoreCalledTimes(expected: Int) {
        assertEquals(expected, restoreCounter)
    }
}

private class FakeAnalyticsCommunication : AnalyticsCommunication {
    private var list = mutableListOf<AnalyticsState>()

    override fun update(value: AnalyticsState) {
        list.add(value)
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: AnalyticsState) {
        assertEquals(expected, list.last())
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AnalyticsState>) {
        TODO("Not yet implemented")
    }

    private var key = ""
    private var bundleWrapper: BundleWrapper.Mutable? = null
    private var saveCounter = 0
    private var restoreCounter = 0

    override fun save(key: String, bundleWrapper: BundleWrapper.Save) {
        this.key = key
        this.bundleWrapper = bundleWrapper as BundleWrapper.Mutable
        saveCounter++
    }

    override fun restore(key: String, bundleWrapper: BundleWrapper.Restore) {
        assertEquals(this.key, key)
        assertEquals(this.bundleWrapper, bundleWrapper)
        restoreCounter++
    }

    fun checkSaveCalledTimes(expected: Int) {
        assertEquals(expected, saveCounter)
    }

    fun checkRestoreCalledTimes(expected: Int) {
        assertEquals(expected, restoreCounter)
    }
}