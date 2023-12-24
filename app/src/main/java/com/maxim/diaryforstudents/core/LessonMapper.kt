package com.maxim.diaryforstudents.core

import com.maxim.diaryforstudents.R

interface LessonMapper {
    fun map(name: String): String

    class Base(private val resources: ManageResource): LessonMapper {
        override fun map(name: String): String {
            val resource = when(name) {
                "algebra" -> R.string.algebra
                "biology" -> R.string.biology
                "history" -> R.string.history
                "french" -> R.string.french
                "geography" -> R.string.geography
                "english_language" -> R.string.english_language
                "informatics" -> R.string.informatics
                "literature" -> R.string.literature
                "music" -> R.string.music
                "social_science" -> R.string.social_science
                "russian_language" -> R.string.russian_language
                "technology" -> R.string.technology
                "physics" -> R.string.physics
                "pe" -> R.string.pe
                "chemistry" -> R.string.chemistry
                else -> R.string.unknown_lesson
            }
            return resources.string(resource)
        }
    }
}