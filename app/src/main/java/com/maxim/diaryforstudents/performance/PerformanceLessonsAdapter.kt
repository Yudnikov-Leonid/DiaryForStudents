package com.maxim.diaryforstudents.performance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.LessonPerformanceBinding

class PerformanceLessonsAdapter : RecyclerView.Adapter<PerformanceLessonsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<PerformanceUi.Lesson>()

    abstract class ItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: PerformanceUi.Lesson)
    }

    class BaseViewHolder(private val binding: LessonPerformanceBinding) : ItemViewHolder(binding) {
        override fun bind(item: PerformanceUi.Lesson) {
            item.showName(binding.lessonNameTextView)
            val adapter = PerformanceGradesAdapter()
            binding.gradesRecyclerView.adapter = adapter
            item.showGrades(adapter)
            item.showAverage(binding.averageTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return BaseViewHolder(
            LessonPerformanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<PerformanceUi.Lesson>) {
        val diff = LessonsDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}

class LessonsDiffUtil(
    private val oldList: List<PerformanceUi.Lesson>,
    private val newList: List<PerformanceUi.Lesson>,
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].sameContent(newList[newItemPosition])
}