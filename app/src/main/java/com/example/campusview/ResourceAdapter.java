package com.example.campusview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {
    private List<Resource> resourceList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onBookClick(Resource resource);
    }

    public ResourceAdapter(List<Resource> resourceList, OnItemClickListener listener) {
        this.resourceList = resourceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resource resource = resourceList.get(position);
        holder.resourceNameTextView.setText(resource.getName());
        holder.resourceTypeTextView.setText("类型: " + resource.getType());
        holder.resourceStatusTextView.setText("状态: " + resource.getStatus());
        holder.resourceDescriptionTextView.setText(resource.getDescription());
        
        holder.bookButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(resource);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView resourceNameTextView;
        TextView resourceTypeTextView;
        TextView resourceStatusTextView;
        TextView resourceDescriptionTextView;
        Button bookButton;

        ViewHolder(View itemView) {
            super(itemView);
            resourceNameTextView = itemView.findViewById(R.id.resourceName);
            resourceTypeTextView = itemView.findViewById(R.id.resourceType);
            resourceStatusTextView = itemView.findViewById(R.id.resourceStatus);
            resourceDescriptionTextView = itemView.findViewById(R.id.resourceDescription);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }
} 