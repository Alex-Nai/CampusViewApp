package com.example.campusview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    private CardView resourceCard;
    private CardView scenicSpotCard;
    private CardView navigationCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        resourceCard = findViewById(R.id.resourceCard);
        scenicSpotCard = findViewById(R.id.scenicSpotCard);
        navigationCard = findViewById(R.id.navigationCard);

        // 设置点击事件
        resourceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResourceActivity.class);
                startActivity(intent);
            }
        });

        scenicSpotCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScenicSpotActivity.class);
                startActivity(intent);
            }
        });

        navigationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });
    }
} 