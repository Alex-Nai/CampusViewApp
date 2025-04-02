package com.example.campusview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.campusview.fragment.ProfileFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.example.campusview.utils.AuthManager;

public class MainActivity extends AppCompatActivity {
    private MaterialCardView resourceCard;
    private MaterialCardView scenicSpotCard;
    private MaterialCardView navigationCard;
    private BottomNavigationView bottomNavigation;
    private MaterialToolbar toolbar;
    private FragmentManager fragmentManager;
    private AuthManager authManager;
    private View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 AuthManager
        authManager = AuthManager.getInstance(this);

        // 初始化视图
        resourceCard = findViewById(R.id.resourceCard);
        scenicSpotCard = findViewById(R.id.scenicSpotCard);
        navigationCard = findViewById(R.id.navigationCard);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.top_app_bar);
        fragmentContainer = findViewById(R.id.fragment_container);

        // 设置 ActionBar
        setSupportActionBar(toolbar);

        // 初始化 FragmentManager
        fragmentManager = getSupportFragmentManager();

        // 设置底部导航栏点击事件
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // 显示主页内容
                resourceCard.setVisibility(View.VISIBLE);
                scenicSpotCard.setVisibility(View.VISIBLE);
                navigationCard.setVisibility(View.VISIBLE);
                fragmentContainer.setVisibility(View.GONE);
                toolbar.setTitle("校园资源管理");
                return true;
            } else if (itemId == R.id.navigation_profile) {
                // 显示个人中心
                resourceCard.setVisibility(View.GONE);
                scenicSpotCard.setVisibility(View.GONE);
                navigationCard.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);
                toolbar.setTitle("个人中心");
                showProfileFragment();
                return true;
            }
            return false;
        });

        // 设置卡片点击事件
        resourceCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResourceActivity.class);
            startActivity(intent);
        });

        scenicSpotCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScenicSpotActivity.class);
            startActivity(intent);
        });

        navigationCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
            startActivity(intent);
        });
    }

    private void showProfileFragment() {
        Fragment profileFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, profileFragment);
            transaction.commit();
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.show(profileFragment);
            transaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 检查登录状态
        if (!authManager.isLoggedIn()) {
            Log.d("MainActivity", "用户未登录，跳转到登录界面");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Log.d("MainActivity", "用户已登录，继续显示主界面");
        }
    }
} 