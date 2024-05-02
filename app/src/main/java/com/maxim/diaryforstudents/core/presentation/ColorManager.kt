package com.maxim.diaryforstudents.core.presentation

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.maxim.diaryforstudents.core.data.SimpleStorage
import javax.inject.Inject

interface ColorManager {
    fun saveColor(color: Int, key: String)
    fun getColor(key: String, default: Int): Int
    fun hasColor(key: String): Boolean
    fun resetColor(key: String)

    fun showColor(imageButton: ImageButton, key: String, defaultColor: Int)
    fun showColor(textView: TextView, key: String, defaultColor: Int)
    fun showStroke(view: View, key: String, defaultColor: Int)

    class Base @Inject constructor(private val simpleStorage: SimpleStorage) : ColorManager {
        override fun saveColor(color: Int, key: String) {
            simpleStorage.save("$KEY$key", color)
        }

        override fun getColor(key: String, default: Int): Int {
            val value = simpleStorage.read("$KEY$key", default)
            return if (value != -1) value
            else default
        }

        override fun hasColor(key: String) = simpleStorage.read("$KEY$key", -1) != -1

        override fun resetColor(key: String) {
            simpleStorage.save("$KEY$key", -1)
        }

        override fun showColor(imageButton: ImageButton, key: String, defaultColor: Int) {
            val color = if (hasColor(key)) getColor(key, 0) else ContextCompat.getColor(
                imageButton.context,
                defaultColor
            )
            imageButton.setBackgroundColor(color)
        }

        override fun showColor(textView: TextView, key: String, defaultColor: Int) {
            val color = if (hasColor(key)) getColor(key, 0) else ContextCompat.getColor(
                textView.context,
                defaultColor
            )
            textView.setTextColor(color)
        }

        override fun showStroke(view: View, key: String, defaultColor: Int) {
            val background = view.background as GradientDrawable
            background.setStroke(8, getColor(key, ContextCompat.getColor(view.context, defaultColor)))
        }

        companion object {
            private const val KEY = "color_"
        }
    }
}