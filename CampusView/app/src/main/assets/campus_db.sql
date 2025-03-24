-- 创建教室资源表
CREATE TABLE IF NOT EXISTS classroom (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    building_type VARCHAR(20) NOT NULL,  -- '主楼' 或 '中楼'
    room_number VARCHAR(10) NOT NULL,
    capacity INT NOT NULL,
    is_available TINYINT(1) DEFAULT 1,  -- BOOLEAN 替换成 TINYINT(1)
    current_status VARCHAR(50),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建资源预约表
CREATE TABLE IF NOT EXISTS resource_booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resource_type VARCHAR(20) NOT NULL,  -- '讲座' 或 '实验室'
    resource_name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    capacity INT,
    start_time DATETIME,
    end_time DATETIME,
    is_available TINYINT(1) DEFAULT 1,
    description TEXT
);

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(200) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',  -- USER, ADMIN
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL DEFAULT NULL
);

-- 创建资源表
CREATE TABLE IF NOT EXISTS resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,  -- classroom, laboratory, lecture_hall, etc.
    location VARCHAR(100),
    capacity INT,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'available',  -- available, unavailable, maintenance
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建预约记录表
CREATE TABLE IF NOT EXISTS booking_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resource_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',  -- pending, approved, cancelled, completed
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remarks TEXT,
    FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 创建景点信息表
CREATE TABLE IF NOT EXISTS scenic_spots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(100),
    image_path VARCHAR(200),
    coordinates VARCHAR(50),  -- 经纬度坐标
    features TEXT,  -- 用于图像匹配的特征数据
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建资源类型枚举表
CREATE TABLE IF NOT EXISTS resource_types (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入基础资源类型数据
INSERT INTO resource_types (name, description) VALUES
('classroom', '普通教室'),
('laboratory', '实验室'),
('lecture_hall', '报告厅'),
('meeting_room', '会议室'),
('study_room', '自习室');

-- 创建索引
CREATE INDEX idx_resources_type ON resources(type);
CREATE INDEX idx_resources_status ON resources(status);
CREATE INDEX idx_booking_records_user ON booking_records(user_id);
CREATE INDEX idx_booking_records_resource ON booking_records(resource_id);
CREATE INDEX idx_booking_records_status ON booking_records(status);
CREATE INDEX idx_booking_records_time ON booking_records(start_time, end_time);

-- 创建触发器：更新资源的updated_at时间戳
CREATE TRIGGER trg_update_resources_updated_at
BEFORE UPDATE ON resources
FOR EACH ROW
SET NEW.updated_at = CURRENT_TIMESTAMP;

-- 创建触发器：更新景点的updated_at时间戳
CREATE TRIGGER trg_update_scenic_spots_updated_at
BEFORE UPDATE ON scenic_spots
FOR EACH ROW
SET NEW.updated_at = CURRENT_TIMESTAMP;
