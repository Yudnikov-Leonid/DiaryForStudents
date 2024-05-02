package com.maxim.diaryforstudents.performance.common.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.databinding.LessonPerformanceBinding
import com.maxim.diaryforstudents.databinding.NoDataBinding

class PerformanceLessonsAdapter(
    private val listener: Listener,
    private val markListener: PerformanceMarksAdapter.Listener,
    private val colorManager: ColorManager
) : RecyclerView.Adapter<PerformanceLessonsAdapter.ItemViewHolder>() {
    private var list = mutableListOf<PerformanceUi>()
    private var progressType: ProgressType = ProgressType.AWeekAgo
    private var showType = true

    abstract class ItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: PerformanceUi, progressType: ProgressType, showType: Boolean) {}
    }

    class BaseViewHolder(
        private val binding: LessonPerformanceBinding,
        private val listener: Listener,
        private val markListener: PerformanceMarksAdapter.Listener,
        private val colorManager: ColorManager
    ) : ItemViewHolder(binding) {
        override fun bind(item: PerformanceUi, progressType: ProgressType, showType: Boolean) {
            item.showName(binding.lessonNameTextView)
            val adapter = PerformanceMarksAdapter(markListener, colorManager)
            binding.marksRecyclerView.adapter = adapter
            item.showMarks(adapter, showType, binding.marksRecyclerView)
            item.showAverage(
                binding.averageTitleTextView,
                binding.averageTextView,
                colorManager
            )
            item.showProgress(
                binding.statusImageView,
                binding.statusDescriptionTextView,
                progressType
            )
            item.showCalculateButton(binding.calculateButton)
            binding.calculateButton.setOnClickListener {
                item.calculate(listener)
            }
            itemView.setOnClickListener {
                item.analytics(listener)
            }
        }
    }

    class EmptyViewHolder(binding: NoDataBinding) : ItemViewHolder(binding)

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is PerformanceUi.Empty) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return if (viewType == 1) BaseViewHolder(
            LessonPerformanceBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener, markListener, colorManager
        ) else EmptyViewHolder(
            NoDataBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], progressType, showType)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<PerformanceUi>, progressType: ProgressType, showType: Boolean) {
        if (this.progressType != progressType || this.showType != showType) {
            this.progressType = progressType
            this.showType = showType
            list.clear()
            list.addAll(newList)
            notifyDataSetChanged()
        } else {
            val diff = PerformanceDiffUtil(list, newList)
            val result = DiffUtil.calculateDiff(diff)
            list.clear()
            list.addAll(newList)
            result.dispatchUpdatesTo(this)
        }
    }

    interface Listener {
        fun calculate(marks: List<PerformanceUi>, marksSum: Int)
        fun analytics(lessonName: String)
    }
}