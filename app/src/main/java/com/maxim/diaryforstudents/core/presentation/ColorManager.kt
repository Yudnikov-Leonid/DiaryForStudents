package com.maxim.diaryforstudents.core.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.maxim.diaryforstudents.core.data.SimpleStorage

interface ColorManager {
    fun saveColor(color: Int, key: String)
    fun getColor(key: String, default: Int): Int
    fun hasColor(key: String): Boolean
    fun resetColor(key: String)

    fun showColor(imageView: ImageView, key: String, defaultColor: Int)
    fun showColor(textView: TextView, key: String, defaultColor: Int)

    class Base(private val simpleStorage: SimpleStorage) : ColorManager {
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

        override fun showColor(imageView: ImageView, key: String, defaultColor: Int) {
            if (hasColor(key))
                imageView.setBackgroundColor(getColor(key, 0))
            else
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        imageView.context,
                        defaultColor
                    )
                )
        }

        override fun showColor(textView: TextView, key: String, defaultColor: Int) {
            if (hasColor(key))
                textView.setTextColor(getColor(key, 0))
            else
                textView.setTextColor(
                    ContextCompat.getColor(
                        textView.context,
                        defaultColor
                    )
                )
        }

        companion object {
            private const val KEY = "color_"
        }
    }
}