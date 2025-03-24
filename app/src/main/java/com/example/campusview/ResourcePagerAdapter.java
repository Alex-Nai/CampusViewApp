package com.example.campusview;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ResourcePagerAdapter extends FragmentStateAdapter {
    public ResourcePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ClassroomFragment();
        } else {
            return new ResourceFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
} 