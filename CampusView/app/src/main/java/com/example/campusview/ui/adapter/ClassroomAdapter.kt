package com.example.campusview.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.campusview.databinding.ItemClassroomBinding
import com.example.campusview.model.ClassroomDto

class ClassroomAdapter : ListAdapter<ClassroomDto, ClassroomAdapter.ClassroomViewHolder>(ClassroomDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomViewHolder {
        val binding = ItemClassroomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClassroomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassroomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ClassroomViewHolder(
        private val binding: ItemClassroomBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(classroom: ClassroomDto) {
            binding.apply {
                roomNumberText.text = classroom.roomNumber
                buildingTypeText.text = classroom.buildingType
                capacityText.text = "容量: ${classroom.capacity}人"
                statusText.text = if (classroom.isAvailable) "可用" else "不可用"
                statusText.setTextColor(
                    if (classroom.isAvailable) android.graphics.Color.GREEN
                    else android.graphics.Color.RED
                )
            }
        }
    }

    private class ClassroomDiffCallback : DiffUtil.ItemCallback<ClassroomDto>() {
        override fun areItemsTheSame(oldItem: ClassroomDto, newItem: ClassroomDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ClassroomDto, newItem: ClassroomDto): Boolean {
            return oldItem == newItem
        }
    }
} 