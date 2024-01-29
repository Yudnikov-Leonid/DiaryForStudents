package com.maxim.diaryforstudents.performance.common.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.LessonPerformanceBinding
import com.maxim.diaryforstudents.databinding.NoDataBinding

class PerformanceLessonsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<PerformanceLessonsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<PerformanceUi>()
    private var progressType: ProgressType = ProgressType.AWeekAgo

    abstract class ItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: PerformanceUi, progressType: ProgressType) {}
    }

    class BaseViewHolder(private val binding: LessonPerformanceBinding, private val listener: Listener) : ItemViewHolder(binding) {
        override fun bind(item: PerformanceUi, progressType: ProgressType) {
            item.showName(binding.lessonNameTextView)
            val adapter = PerformanceMarksAdapter()
            binding.marksRecyclerView.adapter = adapter
            item.showMarks(adapter)
            item.showAverage(binding.averageTitleTextView, binding.averageTextView)
            item.showProgress(binding.statusImageView, binding.statusDescriptionTextView, progressType)
            item.showCalculateButton(binding.calculateButton)
            binding.calculateButton.setOnClickListener {
                item.calculate(listener)
            }
        }
    }

    class EmptyViewHolder(binding: NoDataBinding) : ItemViewHolder(binding)

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is PerformanceUi.Empty) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return if (viewType == 1) BaseViewHolder(
            LessonPerformanceBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener
        ) else EmptyViewHolder(
            NoDataBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], progressType)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<PerformanceUi.Lesson>, progressType: ProgressType) {
        if (this.progressType != progressType) {
            this.progressType = progressType
            list.clear()
            list.addAll(newList)
            notifyDataSetChanged()
        } else {
            this.progressType = progressType
            val diff = PerformanceDiffUtil(list, newList)
            val result = DiffUtil.calculateDiff(diff)
            list.clear()
            list.addAll(newList)
            result.dispatchUpdatesTo(this)
        }
    }

    interface Listener {
        fun calculate(marks: List<PerformanceUi.Mark>, marksSum: Int)
    }
}