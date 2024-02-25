package com.maxim.diaryforstudents.menu

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsScreen
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.diary.presentation.DiaryScreen
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.menu.presentation.MenuCommunication
import com.maxim.diaryforstudents.menu.presentation.MenuState
import com.maxim.diaryforstudents.menu.presentation.MenuViewModel
import com.maxim.diaryforstudents.news.FakeNewsRepository
import com.maxim.diaryforstudents.news.presentation.NewsScreen
import com.maxim.diaryforstudents.performance.FakePerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceScreen
import com.maxim.diaryforstudents.profile.presentation.ProfileScreen
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class MenuViewModelTest {
    private lateinit var navigation: FakeNavigation
    private lateinit var performanceInteractor: FakePerformanceInteractor
    private lateinit var newsRepository: FakeNewsRepository
    private lateinit var communication: FakeMenuCommunication
    private lateinit var viewModel: MenuViewModel
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun before() {
        navigation = FakeNavigation(Order())
        performanceInteractor = FakePerformanceInteractor()
        runAsync = FakeRunAsync()
        communication = FakeMenuCommunication()
        newsRepository = FakeNewsRepository()
        viewModel = MenuViewModel(communication, performanceInteractor, newsRepository, navigation, runAsync)
    }

    @Test
    fun test_init() {
        viewModel.init(true)
        performanceInteractor.checkLoadDataCalledTimes(1)
        newsRepository.checkCalledTimes(1)
        newsRepository.checkCalledWith(viewModel)

        viewModel.init(false)
        performanceInteractor.checkLoadDataCalledTimes(1)
        newsRepository.checkCalledTimes(1)
    }

    @Test
    fun test_reload() {
        newsRepository.checkNewNewsMustReturn(5)
        performanceInteractor.newMarksCountMustReturn(3)
        viewModel.init(true)
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(MenuState.Initial(3, 5))
    }

    @Test
    fun test_diary() {
        viewModel.diary()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(DiaryScreen)
    }

    @Test
    fun test_performance() {
        viewModel.performance()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(PerformanceScreen)
    }

    @Test
    fun test_profile() {
        viewModel.profile()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(ProfileScreen)
    }

    @Test
    fun test_news() {
        viewModel.news()
        newsRepository.checkCheckNewsCalledTimes(1)
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(NewsScreen)
    }

    @Test
    fun test_analytics() {
        viewModel.analytics()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(AnalyticsScreen)
    }

    @Test
    fun test_save_and_restore() {
        val bundleWrapper = FakeBundleWrapper()
        viewModel.save(bundleWrapper)
        performanceInteractor.checkSaveCalledTimes(1)
        communication.checkSaveCalledTimes(1)
        viewModel.restore(bundleWrapper)
        performanceInteractor.checkRestoreCalledTimes(1)
        communication.checkRestoreCalledTimes(1)
    }
}

private class FakeMenuCommunication: MenuCommunication {
    private val list = mutableListOf<MenuState>()

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: MenuState) {
        assertEquals(expected, list.last())
    }

    override fun update(value: MenuState) {
        list.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MenuState>) {
        throw IllegalStateException("not using in tests")
    }

    private var bundleWrapper: BundleWrapper.Mutable? = null
    private var saveCounter = 0
    private var restoreCounter = 0

    fun checkSaveCalledTimes(expected: Int) {
        assertEquals(expected, saveCounter)
    }

    fun checkRestoreCalledTimes(expected: Int) {
        assertEquals(expected, restoreCounter)
    }

    override fun save(key: String, bundleWrapper: BundleWrapper.Save) {
        this.bundleWrapper = bundleWrapper as BundleWrapper.Mutable
        saveCounter++
    }

    override fun restore(key: String, bundleWrapper: BundleWrapper.Restore) {
        assertEquals(bundleWrapper, this.bundleWrapper)
        restoreCounter++
    }
}