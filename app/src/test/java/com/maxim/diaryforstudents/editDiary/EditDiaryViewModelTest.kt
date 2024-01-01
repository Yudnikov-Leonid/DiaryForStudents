package com.maxim.diaryforstudents.editDiary

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.editDiary.common.CreateLessonCache
import com.maxim.diaryforstudents.editDiary.common.SelectedClassCache
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CreateLessonScreen
import com.maxim.diaryforstudents.editDiary.edit.data.EditDiaryRepository
import com.maxim.diaryforstudents.editDiary.edit.data.LessonData
import com.maxim.diaryforstudents.editDiary.edit.data.StudentData
import com.maxim.diaryforstudents.editDiary.edit.presentation.EditDiaryCommunication
import com.maxim.diaryforstudents.editDiary.edit.presentation.EditDiaryState
import com.maxim.diaryforstudents.editDiary.edit.presentation.EditDiaryViewModel
import com.maxim.diaryforstudents.editDiary.edit.presentation.LessonUi
import com.maxim.diaryforstudents.editDiary.edit.presentation.ReloadAfterDismiss
import com.maxim.diaryforstudents.editDiary.edit.presentation.StudentUi
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class EditDiaryViewModelTest {
    private lateinit var viewModel: EditDiaryViewModel
    private lateinit var repository: FakeEditDiaryRepository
    private lateinit var communication: FakeEditDiaryCommunication
    private lateinit var selectedCache: FakeSelectedClassCacheTwo
    private lateinit var createCache: FakeCreateLessonCacheTwo
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClearViewModel
    private lateinit var runAsync: FakeRunAsync
    private lateinit var order: Order

    @Before
    fun before() {
        repository = FakeEditDiaryRepository()
        communication = FakeEditDiaryCommunication()
        selectedCache = FakeSelectedClassCacheTwo()
        createCache = FakeCreateLessonCacheTwo()
        order = Order()
        navigation = FakeNavigation(order)
        clear = FakeClearViewModel(order)
        runAsync = FakeRunAsync()
        viewModel = EditDiaryViewModel(
            repository,
            communication,
            selectedCache,
            createCache,
            navigation,
            clear,
            runAsync
        )
    }

    @Test
    fun test_init() {
        selectedCache.readMustReturn("abc")
        repository.initMustReturn(listOf(LessonData.Students(listOf(StudentData.Title("Algebra")))))

        viewModel.init(true)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(EditDiaryState.Loading)
        createCache.checkCacheAfterDismissCalledTimes(1)
        createCache.checkCacheAfterDismissCalledWith(viewModel)
        repository.checkInitCalledTimes(1)
        repository.checkInitCalledWith("abc")

        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            EditDiaryState.Base(
                listOf(
                    LessonUi.Students(
                        listOf(StudentUi.Title("Algebra"))
                    )
                )
            )
        )

        viewModel.init(false)
        repository.checkInitCalledTimes(1)
        createCache.checkCacheAfterDismissCalledTimes(1)
        communication.checkCalledTimes(2)
    }

    @Test
    fun test_new_lesson() {
        viewModel.newLesson()
        createCache.checkClearLessonCalledTimes(1)
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(CreateLessonScreen)
    }

    @Test
    fun test_edit_lesson() {
        viewModel.editLesson(5, "start", "end", "theme", "homework")
        createCache.checkCacheLessonCalledTimes(1)
        createCache.checkCacheLessonCalledWith(listOf(5, "start", "end", "theme", "homework"))
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(CreateLessonScreen)
    }

    @Test
    fun test_set_grade() {
        viewModel.setGrade(5, "userId", 34)
        repository.checkSetGradeCalledTimes(1)
        repository.checkSetGradeCalledWith(listOf(5, "userId", 34))
        communication.checkSetGradeCalledTimes(1)
        communication.checkSetGradeCalledWith(listOf(5, "userId", 34))
    }

    @Test
    fun test_back() {
        viewModel.back()
        navigation.checkCalledWith(Screen.Pop)
        clear.checkCalledWith(EditDiaryViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_reload() {
        selectedCache.readMustReturn("abc")
        repository.initMustReturn(listOf(LessonData.Students(listOf(StudentData.Title("Algebra")))))

        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(EditDiaryState.Loading)
        repository.checkInitCalledTimes(1)
        repository.checkInitCalledWith("abc")

        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            EditDiaryState.Base(
                listOf(
                    LessonUi.Students(
                        listOf(StudentUi.Title("Algebra"))
                    )
                )
            )
        )
    }
}

private class FakeCreateLessonCacheTwo : CreateLessonCache.Update {
    override fun cacheName(value: String) {
        TODO("Not yet implemented")
    }

    override fun cacheClassId(value: String) {
        TODO("Not yet implemented")
    }

    private val cacheAfterDismissList = mutableListOf<ReloadAfterDismiss>()
    fun checkCacheAfterDismissCalledTimes(expected: Int) {
        assertEquals(expected, cacheAfterDismissList.size)
    }

    fun checkCacheAfterDismissCalledWith(expected: ReloadAfterDismiss) {
        assertEquals(expected, cacheAfterDismissList.last())
    }

    override fun cacheAfterDismiss(value: ReloadAfterDismiss) {
        cacheAfterDismissList.add(value)
    }

    private val cacheLessonList = mutableListOf<List<Any>>()
    fun checkCacheLessonCalledTimes(expected: Int) {
        assertEquals(expected, cacheLessonList.size)
    }

    fun checkCacheLessonCalledWith(expected: List<Any>) {
        assertEquals(expected, cacheLessonList.last())
    }

    override fun cacheLesson(
        date: Int,
        startTime: String,
        endTime: String,
        theme: String,
        homework: String
    ) {
        cacheLessonList.add(listOf(date, startTime, endTime, theme, homework))
    }

    private var clearLessonCounter = 0
    fun checkClearLessonCalledTimes(expected: Int) {
        assertEquals(expected, clearLessonCounter)
    }

    override fun clearLesson() {
        clearLessonCounter++
    }
}

private class FakeSelectedClassCacheTwo : SelectedClassCache.Read {
    private var returnValue = ""
    fun readMustReturn(value: String) {
        returnValue = value
    }

    override fun read() = returnValue
}

private class FakeEditDiaryCommunication : EditDiaryCommunication {
    private val list = mutableListOf<EditDiaryState>()
    private val setGradeList = mutableListOf<List<Any>>()
    fun checkSetGradeCalledTimes(expected: Int) {
        assertEquals(expected, setGradeList.size)
    }

    fun checkSetGradeCalledWith(expected: List<Any>) {
        assertEquals(expected, setGradeList.last())
    }

    override fun setGrade(grade: Int?, userId: String, date: Int) {
        setGradeList.add(listOf(grade!!, userId, date))
    }

    override fun update(value: EditDiaryState) {
        list.add(value)
    }

    fun checkCalledWith(expected: EditDiaryState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<EditDiaryState>) {
        throw IllegalStateException("not using in test")
    }
}

private class FakeEditDiaryRepository : EditDiaryRepository {
    private val initList = mutableListOf<String>()
    private val initReturn = mutableListOf<LessonData>()
    fun initMustReturn(value: List<LessonData>) {
        initReturn.addAll(value)
    }

    fun checkInitCalledTimes(expected: Int) {
        assertEquals(expected, initList.size)
    }

    fun checkInitCalledWith(expected: String) {
        assertEquals(expected, initList.last())
    }

    override suspend fun init(classId: String): List<LessonData> {
        initList.add(classId)
        return initReturn
    }

    private val setGradeList = mutableListOf<List<Any>>()
    fun checkSetGradeCalledTimes(expected: Int) {
        assertEquals(expected, setGradeList.size)
    }

    fun checkSetGradeCalledWith(expected: List<Any>) {
        assertEquals(expected, setGradeList.last())
    }

    override suspend fun setGrade(grade: Int?, userId: String, date: Int) {
        setGradeList.add(listOf(grade!!, userId, date))
    }
}