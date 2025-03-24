package com.example.campusview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ViewHolder> {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_classroom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Classroom classroom = classroomList.get(position);
        holder.classroomNameTextView.setText(classroom.getName());
        holder.classroomTypeTextView.setText("类型: " + classroom.getType());
        holder.classroomStatusTextView.setText("状态: " + classroom.getStatus());
        
        holder.bookButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(classroom);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classroomList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView classroomNameTextView;
        TextView classroomTypeTextView;
        TextView classroomStatusTextView;
        Button bookButton;

        ViewHolder(View itemView) {
            super(itemView);
            classroomNameTextView = itemView.findViewById(R.id.classroomName);
            classroomTypeTextView = itemView.findViewById(R.id.classroomType);
            classroomStatusTextView = itemView.findViewById(R.id.classroomStatus);
        }
    }
} 