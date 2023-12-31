package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.EditGradeBinding
import com.maxim.diaryforstudents.databinding.EditGradeDateBinding
import com.maxim.diaryforstudents.databinding.FinalGradesTitleBinding

class EditGradesAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<EditGradesAdapter.ItemViewHolder>() {
    private val list = mutableListOf<GradeUi>()

    abstract class ItemViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: GradeUi) {}
    }

    class BaseViewHolder(private val binding: EditGradeBinding, private val listener: Listener) :
        ItemViewHolder(binding) {
        override fun bind(item: GradeUi) {
            item.show(binding.editGradeEditText)
            binding.editGradeEditText.addTextChangedListener {
                val grade = if (binding.editGradeEditText.text.toString()
                        .isEmpty()
                ) null else binding.editGradeEditText.text.toString().toInt()
                item.setGrade(listener, grade)
            }
        }
    }

    class DateViewHolder(
        private val binding: EditGradeDateBinding,
        private val listener: Listener
    ) : ItemViewHolder(binding) {
        override fun bind(item: GradeUi) {
            item.show(binding.lessonDateTextView)
            itemView.setOnClickListener {
                item.editLesson(listener)
            }
        }
    }

    class StaticViewHolder(binding: ViewBinding) : ItemViewHolder(binding)

    override fun getItemViewType(position: Int) = when (list[position]) {
        is GradeUi.Base -> 1
        is GradeUi.Date -> 2
        else -> 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            1 -> BaseViewHolder(
                EditGradeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                listener
            )

            2 -> DateViewHolder(
                EditGradeDateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), listener
            )

            else -> StaticViewHolder(
                FinalGradesTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<GradeUi>) {
        val diff = EditGradesDiff(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun setGrade(grade: Int?, userId: String, date: Int)
        fun editLesson(
            date: Int,
            startTime: String,
            endTime: String,
            theme: String,
            homework: String
        )
    }
}

class EditGradesDiff(
    private val oldList: List<GradeUi>,
    private val newList: List<GradeUi>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = false
}