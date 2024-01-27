package com.maxim.diaryforstudents.lessonDetails.data

interface LessonDetailsStorage {
    interface Save {
        fun save(name: String, teacherName: String, topic: String, homework: String, previousHomework: String)
    }

    interface Read {
        fun name(): String
        fun teacherName(): String
        fun topic(): String
        fun homework(): String
        fun previousHomework(): String
    }

    interface Mutable: Save, Read

    class Base: Mutable {
        private var name = ""
        private var teacherName = ""
        private var topic = ""
        private var homework = ""
        private var previousHomework = ""

        override fun save(
            name: String,
            teacherName: String,
            topic: String,
            homework: String,
            previousHomework: String
        ) {
            this.name = name
            this.teacherName = teacherName
            this.topic = topic
            this.homework = homework
            this.previousHomework = previousHomework
        }

        override fun name() = name
        override fun teacherName() = teacherName
        override fun topic() = topic
        override fun homework() = homework
        override fun previousHomework() = previousHomework
    }
}