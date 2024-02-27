package com.maxim.diaryforstudents.diary.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.LessonDiaryBinding
import com.maxim.diaryforstudents.databinding.NoDataBinding

class DiaryLessonsAdapter(
    private val listener: Listener,
) : RecyclerView.Adapter<DiaryLessonsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<DiaryUi>()
    private var actualHomework = true

    abstract class ItemViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: DiaryUi, actualHomework: Boolean) {}
    }

    class BaseItemViewHolder(
        private val binding: LessonDiaryBinding,
        private val listener: Listener
    ) : ItemViewHolder(binding) {
        override fun bind(item: DiaryUi, actualHomework: Boolean) {
            item.showTime(binding.timeTextView)
            item.showNameAndNumber(binding.lessonNameTextView)
            item.showTopic(binding.themeTextView, binding.themeTitle)
            if (actualHomework)
                item.showHomework(binding.homeworkTextView, binding.homeWorkTitle)
            else
                item.showPreviousHomework(binding.homeworkTextView, binding.homeWorkTitle)
            item.showMarks(binding.marksLayout)
            itemView.setOnClickListener {
                listener.openDetails(item as DiaryUi.Lesson)
            }
        }
    }

    class EmptyViewHolder(binding: NoDataBinding) : ItemViewHolder(binding)

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is DiaryUi.Lesson) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            0 -> BaseItemViewHolder(
                LessonDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                listener
            )
            else -> EmptyViewHolder(
                NoDataBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], actualHomework)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<DiaryUi>, actualHomework: Boolean) {
        if (actualHomework != this.actualHomework) {
            this.actualHomework = actualHomework
            notifyDataSetChanged()
        } else
            this.actualHomework = actualHomework
        val diff = DiaryLessonDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun openDetails(item: DiaryUi.Lesson)
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