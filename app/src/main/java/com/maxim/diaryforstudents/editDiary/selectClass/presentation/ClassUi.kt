package com.maxim.diaryforstudents.editDiary.selectClass.presentation

import android.widget.TextView

interface ClassUi {
    fun same(item: ClassUi): Boolean
    fun show(textView: TextView) {}
    fun open(listener: ClassesAdapter.Listener) {}
    data class Base(private val id: String, private val name: String) : ClassUi {
        override fun same(item: ClassUi) = item is Base && item.name == name
        override fun show(textView: TextView) {
            textView.text = name
        }
        override fun open(listener: ClassesAdapter.Listener) {
            listener.openClass(id)
        }
    }

    object Empty: ClassUi {
        override fun same(item: ClassUi) = item is Empty
    }
}