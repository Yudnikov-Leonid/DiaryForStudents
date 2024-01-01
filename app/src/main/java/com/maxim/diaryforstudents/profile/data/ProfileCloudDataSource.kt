package com.maxim.diaryforstudents.profile.data

import com.maxim.diaryforstudents.core.data.LessonMapper
import com.maxim.diaryforstudents.core.service.CloudClass
import com.maxim.diaryforstudents.core.service.CloudUser
import com.maxim.diaryforstudents.core.service.MyUser
import com.maxim.diaryforstudents.core.service.Service

interface ProfileCloudDataSource {
    fun signOut()
    suspend fun getGrade(): GradeResult
    class Base(
        private val service: Service,
        private val clientWrapper: ClientWrapper,
        private val lessonMapper: LessonMapper,
        private val myUser: MyUser
    ) : ProfileCloudDataSource {
        override fun signOut() {
            myUser.signOut(clientWrapper)
        }

        override suspend fun getGrade(): GradeResult {
            val user = service.get("users", myUser.id(), CloudUser::class.java).first().second
            return if (user.lesson != "") {
                GradeResult.Teacher(lessonMapper.map(user.lesson))
            } else if (user.classId != "") {
                GradeResult.Student(
                    service.get("classes", user.classId, CloudClass::class.java).first().second.name
                )
            } else GradeResult.Empty
        }
    }
}