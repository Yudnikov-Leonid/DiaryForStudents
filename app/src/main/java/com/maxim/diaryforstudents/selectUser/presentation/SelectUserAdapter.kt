package com.maxim.diaryforstudents.selectUser.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.databinding.SelectUserLayoutBinding

class SelectUserAdapter(private val listener: Listener) :
    RecyclerView.Adapter<SelectUserAdapter.ItemViewHolder>() {
    private val list = mutableListOf<SelectUserUi>()

    class ItemViewHolder(
        private val binding: SelectUserLayoutBinding,
        private val listener: Listener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SelectUserUi, position: Int) {
            item.showName(binding.nameTextView)
            item.showSchool(binding.schoolNameTextView)
            itemView.setOnClickListener {
                listener.select(position)
            }
        }
    }

    interface Listener {
        fun select(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            SelectUserLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<SelectUserUi>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}