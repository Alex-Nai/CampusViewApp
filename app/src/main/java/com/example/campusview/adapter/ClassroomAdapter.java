package com.example.campusview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campusview.R;
import com.example.campusview.model.Classroom;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder> {
    private List<Classroom> classroomList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onBookClick(Classroom classroom);
        void onCancelClick(Classroom classroom);
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
        private TextView classroomName;
        private TextView classroomType;
        private TextView classroomLocation;
        private TextView classroomCapacity;
        private MaterialButton bookButton;
        private MaterialButton cancelButton;

        public ClassroomViewHolder(@NonNull View itemView) {
            super(itemView);
            classroomName = itemView.findViewById(R.id.classroomName);
            classroomType = itemView.findViewById(R.id.classroomType);
            classroomLocation = itemView.findViewById(R.id.classroomLocation);
            classroomCapacity = itemView.findViewById(R.id.classroomCapacity);
            bookButton = itemView.findViewById(R.id.bookButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }

        public void bind(Classroom classroom, OnItemClickListener listener) {
            classroomName.setText(classroom.getName());
            classroomType.setText(classroom.getType());
            classroomLocation.setText(classroom.getLocation());
            classroomCapacity.setText("容量: " + classroom.getCapacity() + "人");

            if (classroom.isAvailable()) {
                bookButton.setEnabled(true);
                bookButton.setText("预约");
                cancelButton.setEnabled(false);
                cancelButton.setText("取消预约");
            } else {
                bookButton.setEnabled(false);
                bookButton.setText("已预约");
                cancelButton.setEnabled(true);
                cancelButton.setText("取消预约");
            }

            bookButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(classroom);
                }
            });

            cancelButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCancelClick(classroom);
                }
            });
        }
    }
} 