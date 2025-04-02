package com.example.campusview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campusview.R;
import com.example.campusview.model.Resource;
import com.example.campusview.utils.AuthManager;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {
    private List<Resource> resourceList;
    private OnItemClickListener listener;
    private BookingStatusProvider bookingStatusProvider;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Resource resource);

        void onBookClick(Resource resource);
        void onCancelClick(Resource resource);
    }

    public interface BookingStatusProvider {
        boolean isResourceBooked(Long resourceId);
    }

    public ResourceAdapter(List<Resource> resourceList, OnItemClickListener listener, BookingStatusProvider bookingStatusProvider) {
        this.resourceList = resourceList;
        this.listener = listener;
        this.bookingStatusProvider = bookingStatusProvider;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resource resource = resourceList.get(position);
        holder.bind(resource, listener, bookingStatusProvider);
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    public void updateData(List<Resource> newResourceList) {
        this.resourceList = newResourceList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView descriptionTextView;
        private TextView locationTextView;
        private TextView typeTextView;
        private MaterialButton bookButton;
        private MaterialButton cancelButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            bookButton = itemView.findViewById(R.id.bookButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }

        public void bind(Resource resource, OnItemClickListener listener, BookingStatusProvider bookingStatusProvider) {
            nameTextView.setText(resource.getName());
            descriptionTextView.setText(resource.getDescription());
            locationTextView.setText(resource.getLocation());
            typeTextView.setText(resource.getType());

            boolean isBooked = bookingStatusProvider.isResourceBooked(resource.getId());
            boolean isAvailable = resource.getAvailable() != null && resource.getAvailable();
            boolean isAdmin = AuthManager.getInstance(itemView.getContext()).hasRole("ADMIN");

            if (isBooked) {
                bookButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.VISIBLE);
                cancelButton.setOnClickListener(v -> listener.onCancelClick(resource));
            } else {
                bookButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.GONE);
                bookButton.setOnClickListener(v -> listener.onBookClick(resource));
            }

            bookButton.setEnabled(isAvailable);
            cancelButton.setEnabled(true);

            String statusText = isAvailable ? "可用" : "不可用";
            if (isBooked) {
                statusText = "已预约";
            }
            typeTextView.setText(resource.getType() + " - " + statusText);
        }
    }
} 