package com.example.campusview;

import android.app.Application;
import com.amap.api.maps.MapsInitializer;

public class CampusViewApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // 初始化高德地图
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 