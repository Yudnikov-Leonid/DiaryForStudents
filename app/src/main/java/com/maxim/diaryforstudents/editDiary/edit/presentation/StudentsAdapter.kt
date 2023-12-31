package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.databinding.EditStudentRecyclerBinding

class StudentsAdapter(
    private val listener: EditGradesAdapter.Listener
) : RecyclerView.Adapter<StudentsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<LessonUi>()

    abstract class ItemViewHolder(binding: EditStudentRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: LessonUi)
    }

    class NamesViewHolder(private val binding: EditStudentRecyclerBinding) :
        ItemViewHolder(binding) {
        override fun bind(item: LessonUi) {
            val adapter = StudentNamesAdapter()
            binding.recyclerView.adapter = adapter
            item.showNames(adapter)
        }
    }

    class GradesViewHolder(private val binding: EditStudentRecyclerBinding, private val listener: EditGradesAdapter.Listener) :
        ItemViewHolder(binding) {
        override fun bind(item: LessonUi) {
            val adapter = EditGradesAdapter(listener)
            binding.recyclerView.adapter = adapter
            item.showLessonsAndGrades(adapter)
        }
    }

    override fun getItemViewType(position: Int) = if (list[position] is LessonUi.Students) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = EditStudentRecyclerBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return if (viewType == 0) NamesViewHolder(binding) else GradesViewHolder(binding, listener)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<LessonUi>) {
        val diff = StudentsDiff(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}

class StudentsDiff(
    private val oldList: List<LessonUi>,
    private val newList: List<LessonUi>,
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = false
}