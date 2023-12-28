package com.maxim.diaryforstudents.performance.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.LessonPerformanceBinding
import com.maxim.diaryforstudents.databinding.NoDataBinding

class PerformanceLessonsAdapter : RecyclerView.Adapter<PerformanceLessonsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<PerformanceUi>()

    abstract class ItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: PerformanceUi) {}
    }

    class BaseViewHolder(private val binding: LessonPerformanceBinding) : ItemViewHolder(binding) {
        override fun bind(item: PerformanceUi) {
            item.showName(binding.lessonNameTextView)
            val adapter = PerformanceGradesAdapter()
            binding.gradesRecyclerView.adapter = adapter
            item.showGrades(adapter)
            item.showAverage(binding.averageTitleTextView, binding.averageTextView)
        }
    }

    class EmptyViewHolder(binding: NoDataBinding) : ItemViewHolder(binding)

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is PerformanceUi.Empty) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return if (viewType == 1) BaseViewHolder(
            LessonPerformanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) else EmptyViewHolder(
            NoDataBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<PerformanceUi.Lesson>) {
        val diff = PerformanceDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}