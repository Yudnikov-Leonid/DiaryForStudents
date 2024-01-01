package com.maxim.diaryforstudents.editDiary

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.sl.ManageResource
import com.maxim.diaryforstudents.editDiary.common.CreateLessonCache
import com.maxim.diaryforstudents.editDiary.createLesson.data.CreateLessonRepository
import com.maxim.diaryforstudents.editDiary.createLesson.data.CreateResult
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CreateLessonCommunication
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CreateLessonState
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CreateLessonViewModel
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.UiValidator
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.ValidationException
import com.maxim.diaryforstudents.editDiary.edit.data.GradeData
import com.maxim.diaryforstudents.editDiary.edit.presentation.ReloadAfterDismiss
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeManageResources
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.Order
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CreateLessonViewModelTest {
    private lateinit var viewModel: CreateLessonViewModel
    private lateinit var repository: FakeCreateLessonRepository
    private lateinit var communication: FakeCreateLessonCommunication
    private lateinit var cache: FakeCreateLessonCache
    private lateinit var validator: FakeUiValidator
    private lateinit var clear: FakeClearViewModel
    private lateinit var resource: FakeManageResources
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun before() {
        repository = FakeCreateLessonRepository()
        communication = FakeCreateLessonCommunication()
        cache = FakeCreateLessonCache()
        validator = FakeUiValidator()
        clear = FakeClearViewModel(Order())
        resource = FakeManageResources("")
        runAsync = FakeRunAsync()
        viewModel = CreateLessonViewModel(repository, communication, cache, validator, clear, resource, runAsync)
    }

    @Test
    fun test_clear() {
        viewModel.clear()
        clear.checkCalledTimes(1)
        clear.checkCalledWith(CreateLessonViewModel::class.java)
    }

    @Test
    fun test_reloadList() {
        val afterDismiss = FakeAfterDismiss()
        cache.afterDismissMustReturn(afterDismiss)

        viewModel.reloadList()
        afterDismiss.checkCalledTimes(1)
    }

    @Test
    fun test_save_incorrect_start_time() {
        validator.mustThrowFirst(ValidationException("Empty start time field"))
        viewModel.save("start", "end", "theme", "homework")
        validator.checkCalledTimes(1)
        validator.checkCalledWith("start")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(CreateLessonState.StartTimeError("Empty start time field"))
    }

    @Test
    fun test_save_incorrect_end_time() {
        validator.mustThrowSecond(ValidationException("Empty end time field"))
        viewModel.save("start", "end", "theme", "homework")
        validator.checkCalledTimes(2)
        validator.checkCalledWith("end")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(CreateLessonState.EndTimeError("Empty end time field"))
    }

    @Test
    fun test_save_correct_create() {
        cache.lessonMustReturn(null)
        cache.nameMustReturn("name")
        cache.classIdMustReturn("classId")
        repository.mustReturn(CreateResult.Success)

        viewModel.save("save", "end", "theme", "homework")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(CreateLessonState.Loading)
        repository.checkCreateCalledTimes(1)
        repository.checkCreateCalledWith(
            listOf(
                "save",
                "end",
                "theme",
                "homework",
                "name",
                "classId",
                resource
            )
        )
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(CreateLessonState.Success)

        repository.mustReturn(CreateResult.Failure("error"))

        viewModel.save("save", "end", "theme", "homework")
        communication.checkCalledTimes(3)
        communication.checkCalledWith(CreateLessonState.Loading)
        repository.checkCreateCalledTimes(2)
        repository.checkCreateCalledWith(
            listOf(
                "save",
                "end",
                "theme",
                "homework",
                "name",
                "classId",
                resource
            )
        )
        runAsync.returnResult()
        communication.checkCalledTimes(4)
        communication.checkCalledWith(CreateLessonState.Error("error"))
    }

    @Test
    fun test_save_correct_update() {
        cache.lessonMustReturn(GradeData.Date(55, "start", "end", "theme", "homework"))
        cache.nameMustReturn("name")
        cache.classIdMustReturn("classId")
        repository.mustReturn(CreateResult.Success)

        viewModel.save("save1", "end1", "theme1", "homework1")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(CreateLessonState.Loading)
        repository.checkUpdateCalledTimes(1)
        repository.checkUpdateCalledWith(
            listOf(
                0, //because need init
                "save1",
                "end1",
                "theme1",
                "homework1",
                "name",
                "classId"
            )
        )
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(CreateLessonState.Success)

        repository.mustReturn(CreateResult.Failure("error"))

        viewModel.save("save1", "end1", "theme1", "homework1")
        communication.checkCalledTimes(3)
        communication.checkCalledWith(CreateLessonState.Loading)
        repository.checkUpdateCalledTimes(2)
        repository.checkUpdateCalledWith(
            listOf(
                0,
                "save1",
                "end1",
                "theme1",
                "homework1",
                "name",
                "classId"
            )
        )
        runAsync.returnResult()
        communication.checkCalledTimes(4)
        communication.checkCalledWith(CreateLessonState.Error("error"))
    }
}

private class FakeUiValidator : UiValidator {
    private var counter = 0
    private var firstException: Exception? = null
    private var secondException: Exception? = null
    private val list = mutableListOf<String>()
    fun mustThrowFirst(value: Exception) {
        firstException = value
    }

    fun mustThrowSecond(value: Exception) {
        secondException = value
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: String) {
        assertEquals(expected, list.last())
    }

    override fun isValid(value: String) {
        list.add(value)
        counter++
        if (counter == 1 && firstException != null) throw firstException!!
        else if (counter == 2 && secondException != null) throw secondException!!
    }
}

private class FakeCreateLessonCache : CreateLessonCache.Read {
    private var lessonReturn: GradeData.Date? = null
    private var name: String = ""
    private var classId: String = ""
    private var afterDismiss: ReloadAfterDismiss? = null
    fun lessonMustReturn(value: GradeData.Date?) {
        lessonReturn = value
    }

    fun nameMustReturn(value: String) {
        name = value
    }

    fun classIdMustReturn(value: String) {
        classId = value
    }

    fun afterDismissMustReturn(value: ReloadAfterDismiss) {
        afterDismiss = value
    }

    override fun name() = name

    override fun classId() = classId

    override fun afterDismiss() = afterDismiss!!

    override fun lesson(): GradeData.Date? = lessonReturn
}

private class FakeCreateLessonCommunication : CreateLessonCommunication {
    private val list = mutableListOf<CreateLessonState>()
    override fun update(value: CreateLessonState) {
        list.add(value)
    }

    fun checkCalledWith(expected: CreateLessonState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<CreateLessonState>) {
        throw IllegalStateException("not using in test")
    }
}

private class FakeCreateLessonRepository : CreateLessonRepository {
    private var mustReturn: CreateResult? = null
    fun mustReturn(value: CreateResult) {
        mustReturn = value
    }

    private val createList = mutableListOf<List<Any>>()
    fun checkCreateCalledTimes(expected: Int) {
        assertEquals(expected, createList.size)
    }

    fun checkCreateCalledWith(expected: List<Any>) {
        assertEquals(expected, createList.last())
    }

    override suspend fun create(
        startTime: String,
        endTime: String,
        theme: String,
        homework: String,
        name: String,
        classId: String,
        resource: ManageResource
    ): CreateResult {
        createList.add(listOf(startTime, endTime, theme, homework, name, classId, resource))
        return mustReturn!!
    }

    private val updateList = mutableListOf<List<Any>>()
    fun checkUpdateCalledTimes(expected: Int) {
        assertEquals(expected, updateList.size)
    }

    fun checkUpdateCalledWith(expected: List<Any>) {
        assertEquals(expected, updateList.last())
    }

    override suspend fun update(
        date: Int,
        startTime: String,
        endTime: String,
        theme: String,
        homework: String,
        name: String,
        classId: String
    ): CreateResult {
        updateList.add(listOf(date, startTime, endTime, theme, homework, name, classId))
        return mustReturn!!
    }
}

private class FakeAfterDismiss : ReloadAfterDismiss {
    private var counter = 0
    override fun reload() {
        counter++
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, counter)
    }
}