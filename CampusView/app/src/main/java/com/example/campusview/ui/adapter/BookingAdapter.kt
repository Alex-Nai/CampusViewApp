package com.example.campusview.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.campusview.databinding.ItemBookingBinding
import com.example.campusview.model.BookingRecordDto
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class BookingAdapter(
    private val onCancelClick: (BookingRecordDto) -> Unit
) : ListAdapter<BookingRecordDto, BookingAdapter.BookingViewHolder>(BookingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookingViewHolder(
        private val binding: ItemBookingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(booking: BookingRecordDto) {
            binding.apply {
                resourceNameText.text = booking.resourceName
                startTimeText.text = "开始时间：${booking.startTime?.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))}"
                endTimeText.text = "结束时间：${booking.endTime?.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))}"
                statusText.text = when (booking.status) {
                    "PENDING" -> "待确认"
                    "CONFIRMED" -> "已确认"
                    "CANCELLED" -> "已取消"
                    else -> booking.status
                }

                cancelButton.setOnClickListener {
                    onCancelClick(booking)
                }
            }
        }
    }

    private class BookingDiffCallback : DiffUtil.ItemCallback<BookingRecordDto>() {
        override fun areItemsTheSame(oldItem: BookingRecordDto, newItem: BookingRecordDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookingRecordDto, newItem: BookingRecordDto): Boolean {
            return oldItem == newItem
        }
    }
} 