package com.example.campusview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campusview.R;
import com.example.campusview.model.Resource;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder> {
    private List<Resource> resourceList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Resource resource);
        void onBookClick(Resource resource);
    }

    public ResourceAdapter(List<Resource> resourceList, OnItemClickListener listener) {
        this.resourceList = resourceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resource, parent, false);
        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, int position) {
        Resource resource = resourceList.get(position);
        holder.bind(resource, listener);
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    public void updateData(List<Resource> newResourceList) {
        this.resourceList = newResourceList;
        notifyDataSetChanged();
    }

    static class ResourceViewHolder extends RecyclerView.ViewHolder {
        private TextView resourceName;
        private TextView resourceType;
        private TextView resourceLocation;
        private TextView resourceDescription;
        private TextView resourceStatus;
        private MaterialButton bookButton;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);
            resourceName = itemView.findViewById(R.id.resourceName);
            resourceType = itemView.findViewById(R.id.resourceType);
            resourceLocation = itemView.findViewById(R.id.resourceLocation);
            resourceDescription = itemView.findViewById(R.id.resourceDescription);
            resourceStatus = itemView.findViewById(R.id.resourceStatus);
            bookButton = itemView.findViewById(R.id.bookButton);
        }

        public void bind(final Resource resource, final OnItemClickListener listener) {
            resourceName.setText(resource.getName());
            resourceType.setText(resource.getType());
            resourceLocation.setText(resource.getLocation());
            resourceDescription.setText(resource.getDescription());
            resourceStatus.setText(resource.isAvailable() ? "可用" : "已占用");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(resource);
                }
            });

            bookButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(resource);
                }
            });
        }
    }
} 