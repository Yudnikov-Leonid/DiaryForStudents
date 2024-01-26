package com.maxim.diaryforstudents.diary.data
//
//import com.maxim.diaryforstudents.core.data.LessonMapper
//import com.maxim.diaryforstudents.core.presentation.Reload
//import com.maxim.diaryforstudents.core.service.CloudLesson
//import com.maxim.diaryforstudents.core.service.CloudUser
//import com.maxim.diaryforstudents.core.service.MyUser
//import com.maxim.diaryforstudents.core.service.Service
//import com.maxim.diaryforstudents.core.service.ServiceValueEventListener
//
//interface DiaryCloudDataSource {
//    suspend fun init(reload: Reload, week: Int)
//    fun data(): List<DiaryData.Lesson>
//
//    class Base(
//        private val service: Service,
//        private val myUser: MyUser,
//        private val mapper: LessonMapper
//    ) : DiaryCloudDataSource {
//        private val data = mutableListOf<DiaryData.Lesson>()
//        override suspend fun init(reload: Reload, week: Int) {
//            val classId =
//                service.get("users", myUser.id(), CloudUser::class.java).first().second.classId
//            service.listenByChild("lessons", "week", week.toDouble(), CloudLesson::class.java,
//                object : ServiceValueEventListener<CloudLesson> {
//                    override fun valueChanged(value: List<Pair<String, CloudLesson>>) {
//                        data.clear()
//                        data.addAll(value.filter { it.second.classId == classId }
//                            .sortedBy { it.second.startTime }
//                            .map {
//                                DiaryData.Lesson(
//                                    mapper.map(it.second.name),
//                                    it.second.theme,
//                                    it.second.homework,
//                                    it.second.startTime,
//                                    it.second.endTime,
//                                    it.second.date
//                                )
//                            })
//                        reload.reload()
//                    }
//
//                    override fun error(message: String) = reload.error(message)
//                })
//        }
//
//        override fun data(): List<DiaryData.Lesson> = data
//    }
//}