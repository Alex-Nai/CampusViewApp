# 校内研学参观 APP

**版本：1.0.0**  
**更新日期：2025-03-24**  

## 项目简介
本项目是一个校内研学参观 APP，旨在提供资源管理、图像匹配识别以及导航功能，帮助用户更好地探索校园。

## 功能介绍

### 1. 校内资源管理
- 查看 **主楼**、**中楼** 教室的可用情况（不可预约，仅查看）。
- 查看并预约 **讲座**、**实验室** 资源。
- 后端数据库存储资源信息，前端获取并展示数据。

### 2. 以图搜景
- 用户上传图片。
- 后端比对数据库中的景点信息。
- 返回匹配的景点介绍。

### 3. 图像导航
- 用户上传图片并输入目的地。
- 通过地图 API 提供导航路线。

## 技术栈
- **前端：** Android（Kotlin/Java）+ Retrofit + RecyclerView
- **后端：** Spring Boot + MySQL + RESTful API
- **数据库：** MySQL 8.x
- **地图服务：** 高德地图 API

## 安装与运行
```bash
git clone https://github.com/your-repo-name.git
cd your-repo-name
# 后端运行
cd backend
mvn spring-boot:run
# 前端运行（需 Android Studio）
cd ../frontend
# 使用 Android Studio 编译并运行
```

## 贡献
@NFan
---

# Campus Study Tour APP

**Version: 1.0.0**  
**Last Updated: 2025-03-24**  

## Introduction
This project is a campus study tour APP that provides resource management, image-based recognition, and navigation features to help users explore the campus efficiently.

## Features

### 1. Campus Resource Management
- View the availability of **Main Building** and **Middle Building** classrooms (view-only, no booking).
- View and book resources for **Lectures** and **Laboratories**.
- Backend database stores resource information, and the frontend fetches and displays data.

### 2. Image-Based Spot Recognition
- Users upload an image.
- The backend compares it with the scenic spots database.
- Returns the matched scenic spot details.

### 3. Image-Based Navigation
- Users upload an image and enter a destination.
- Provides navigation routes using a map API.

## Tech Stack
- **Frontend:** Android (Kotlin/Java) + Retrofit + RecyclerView
- **Backend:** Spring Boot + MySQL + RESTful API
- **Database:** MySQL 8.x
- **Map Services:** AMap (Gaode) API

## Installation & Running
```bash
git clone https://github.com/your-repo-name.git
cd your-repo-name
# Run Backend
cd backend
mvn spring-boot:run
# Run Frontend (requires Android Studio)
cd ../frontend
# Compile and run using Android Studio
```

## Contribution
@NFan
