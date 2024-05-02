package com.maxim.diaryforstudents.performance.finalMarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.databinding.FinalLessonBinding
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

class PerformanceFinalLessonsAdapter(private val colorManager: ColorManager) :
    RecyclerView.Adapter<PerformanceFinalLessonsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<PerformanceUi>()

    class ItemViewHolder(private val binding: FinalLessonBinding, private val colorManager: ColorManager) : ViewHolder(binding.root) {
        fun bind(item: PerformanceUi) {
            item.showName(binding.lessonNameTextView)
            listOf(binding.one, binding.two, binding.three, binding.four, binding.five).forEach {
                it.text = ""
            }
            item.showFinalMarks(
                binding.one,
                binding.two,
                binding.three,
                binding.four,
                binding.five,
               colorManager
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            FinalLessonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), colorManager
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<PerformanceUi>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}