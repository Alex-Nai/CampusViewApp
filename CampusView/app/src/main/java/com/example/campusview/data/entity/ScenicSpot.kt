package com.example.campusview.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scenic_spots")
data class ScenicSpot(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val location: String?,
    val imagePath: String?,
    val coordinates: String?, // 经纬度坐标
    val features: String? // 用于图像匹配的特征数据
) 