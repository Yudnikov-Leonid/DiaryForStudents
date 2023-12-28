package com.maxim.diaryforstudents.diary.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.LessonDiaryBinding
import com.maxim.diaryforstudents.databinding.NoDataBinding

class DiaryLessonsAdapter : RecyclerView.Adapter<DiaryLessonsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<DiaryUi>()

    abstract class ItemViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: DiaryUi) {}
    }

    class BaseItemViewHolder(private val binding: LessonDiaryBinding) : ItemViewHolder(binding) {
        override fun bind(item: DiaryUi) {
            item.showTime(binding.timeTextView)
            item.showName(binding.lessonNameTextView)
            item.showTheme(binding.themeTextView, binding.themeTitle)
            item.showHomework(binding.homeworkTextView, binding.homeWorkTitle)
        }
    }

    class EmptyViewHolder(binding: NoDataBinding) : ItemViewHolder(binding)

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is DiaryUi.Lesson) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == 0) BaseItemViewHolder(
            LessonDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) else EmptyViewHolder(
            NoDataBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<DiaryUi>) {
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