package com.maxim.diaryforstudents.menu.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.databinding.MenuLessonLayoutBinding
import com.maxim.diaryforstudents.diary.presentation.DiaryUi

class MenuLessonsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<MenuLessonsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<DiaryUi.Lesson>()
    private var currentLesson = 0
    private var isBreak = false

    class ItemViewHolder(
        private val binding: MenuLessonLayoutBinding,
        private val listener: Listener,
        private val isBreak: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiaryUi.Lesson, pos: Int, currentLesson: Int) {
            item.showNameAndNumber(binding.lessonName)
            item.showTime(binding.time)
            item.showPreviousHomework(binding.previousHomework, binding.previousHomeworkTitle)
            if (pos == currentLesson) {
                binding.title.visibility = View.VISIBLE
                binding.indicator.visibility = View.VISIBLE
                if (isBreak) {
                    binding.title.text = binding.time.context.getString(R.string.lesson_starts_soon)
                    binding.indicator.setImageResource(R.drawable.wait_24)
                } else {
                    binding.title.text = binding.time.context.getString(R.string.is_going_on_now)
                    binding.indicator.setImageResource(android.R.drawable.presence_online)
                }
            } else if (pos - 1 == currentLesson) {
                binding.title.visibility = View.VISIBLE
                binding.indicator.visibility = View.GONE
                binding.title.text = binding.time.context.getString(R.string.next_lesson)
            } else if (pos < currentLesson) {
                binding.indicator.visibility = View.VISIBLE
                binding.title.visibility = View.VISIBLE
                binding.title.text = binding.time.context.getString(R.string.lesson_passed)
                binding.indicator.setImageResource(R.drawable.done_24)
            } else {
                binding.indicator.visibility = View.GONE
                binding.title.visibility = View.GONE
            }
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
            ), listener, isBreak
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], position, currentLesson)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<DiaryUi.Lesson>, currentLesson: Int, isBreak: Boolean) {
        this.currentLesson = currentLesson
        this.isBreak = isBreak
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    interface Listener {
        fun details(item: DiaryUi.Lesson)
    }
}