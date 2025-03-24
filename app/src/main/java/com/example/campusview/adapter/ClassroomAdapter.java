package com.example.campusview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campusview.R;
import com.example.campusview.model.Classroom;
import java.util.List;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder> {
    private List<Classroom> classroomList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onBookClick(Classroom classroom);
    }

    public ClassroomAdapter(List<Classroom> classroomList, OnItemClickListener listener) {
        this.classroomList = classroomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_classroom, parent, false);
        return new ClassroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassroomViewHolder holder, int position) {
        Classroom classroom = classroomList.get(position);
        holder.bind(classroom, listener);
    }

    @Override
    public int getItemCount() {
        return classroomList.size();
    }

    public void updateData(List<Classroom> newClassroomList) {
        this.classroomList = newClassroomList;
        notifyDataSetChanged();
    }

    static class ClassroomViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView typeTextView;
        private TextView locationTextView;
        private TextView capacityTextView;
        private TextView statusTextView;

        public ClassroomViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.classroomName);
            typeTextView = itemView.findViewById(R.id.classroomType);
            locationTextView = itemView.findViewById(R.id.classroomLocation);
            capacityTextView = itemView.findViewById(R.id.classroomCapacity);
            statusTextView = itemView.findViewById(R.id.classroomStatus);
        }

        public void bind(final Classroom classroom, final OnItemClickListener listener) {
            nameTextView.setText(classroom.getName());
            typeTextView.setText(classroom.getType());
            locationTextView.setText(classroom.getLocation());
            capacityTextView.setText(String.format("容量: %d人", classroom.getCapacity()));
            statusTextView.setText(classroom.isAvailable() ? "可用" : "已占用");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(classroom);
                }
            });
        }
    }
} 