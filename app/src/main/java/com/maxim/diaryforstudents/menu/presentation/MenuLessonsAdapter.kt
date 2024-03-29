package com.maxim.diaryforstudents.menu.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.databinding.MenuLessonLayoutBinding
import com.maxim.diaryforstudents.diary.presentation.DiaryLessonDiffUtil
import com.maxim.diaryforstudents.diary.presentation.DiaryUi

class MenuLessonsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<MenuLessonsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<DiaryUi.Lesson>()

    class ItemViewHolder(
        private val binding: MenuLessonLayoutBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiaryUi.Lesson) {
            item.showNameAndNumber(binding.lessonName)
            item.showTime(binding.time)
            item.showPreviousHomework(binding.previousHomework, binding.previousHomeworkTitle)
            item.showInMenu(binding.indicator, binding.status)
            itemView.setOnClickListener {
                listener.details(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            MenuLessonLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<DiaryUi.Lesson>) {
        val diff = DiaryLessonDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun details(item: DiaryUi.Lesson)
    }
}