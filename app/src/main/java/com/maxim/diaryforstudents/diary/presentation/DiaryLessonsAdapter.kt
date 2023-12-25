package com.maxim.diaryforstudents.diary.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.databinding.LessonDiaryBinding

class DiaryLessonsAdapter : RecyclerView.Adapter<DiaryLessonsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<DiaryUi.Lesson>()

    class ItemViewHolder(private val binding: LessonDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiaryUi) {
            item.showTime(binding.timeTextView)
            item.showName(binding.lessonNameTextView)
            item.showTheme(binding.themeTextView, binding.themeTitle)
            item.showHomework(binding.homeworkTextView, binding.homeWorkTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            LessonDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

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
}

class DiaryLessonDiffUtil(
    private val oldList: List<DiaryUi>,
    private val newList: List<DiaryUi>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].sameContent(newList[newItemPosition])
}