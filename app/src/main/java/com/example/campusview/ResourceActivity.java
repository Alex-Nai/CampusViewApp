package com.example.campusview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ResourceActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        // 设置Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("校内资源管理");

        // 初始化ViewPager和TabLayout
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // 设置ViewPager适配器
        ResourcePagerAdapter pagerAdapter = new ResourcePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // 连接TabLayout和ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> {
                if (position == 0) {
                    tab.setText("教室");
                } else {
                    tab.setText("资源");
                }
            }
        ).attach();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 