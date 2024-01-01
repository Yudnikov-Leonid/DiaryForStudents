package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.LessonTitleBinding
import com.maxim.diaryforstudents.databinding.StudentNameBinding

class StudentNamesAdapter : RecyclerView.Adapter<StudentNamesAdapter.ItemViewHolder>() {
    private val list = mutableListOf<StudentUi>()

    abstract class ItemViewHolder(binding: ViewBinding, private val textView: TextView) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StudentUi) {
            item.showName(textView)
        }
    }

    class BaseViewHolder(binding: StudentNameBinding) :
        ItemViewHolder(binding, binding.studentNameTextView)

    class TitleViewHolder(binding: LessonTitleBinding) :
        ItemViewHolder(binding, binding.studentNameTextView)

    override fun getItemViewType(position: Int) = if (list[position] is StudentUi.Base) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == 0) BaseViewHolder(
            StudentNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) else TitleViewHolder(
            LessonTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<StudentUi>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}