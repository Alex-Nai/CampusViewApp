-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    email TEXT,
    role TEXT DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建教室表
CREATE TABLE IF NOT EXISTS classrooms (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    building_type TEXT NOT NULL,
    room_number TEXT NOT NULL,
    capacity INTEGER NOT NULL,
    is_available BOOLEAN DEFAULT 1,
    current_status TEXT,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(building_type, room_number)
);

-- 创建资源表
CREATE TABLE IF NOT EXISTS resources (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    resource_type TEXT NOT NULL,
    resource_name TEXT NOT NULL,
    location TEXT,
    capacity INTEGER,
    description TEXT,
    is_available BOOLEAN DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建预约记录表
CREATE TABLE IF NOT EXISTS booking_records (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    resource_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    status TEXT DEFAULT 'pending',
    remarks TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_booking_records_user_id ON booking_records(user_id);
CREATE INDEX IF NOT EXISTS idx_booking_records_resource_id ON booking_records(resource_id);
CREATE INDEX IF NOT EXISTS idx_classrooms_building_type ON classrooms(building_type);
CREATE INDEX IF NOT EXISTS idx_resources_type ON resources(resource_type);

-- 插入测试数据
INSERT INTO users (username, password, email, role) VALUES
('admin', '$2a$10$rDkPvvAFV6GgJkKqX3q3/.KqX3q3/.KqX3q3/.KqX3q3/.KqX3q3/', 'admin@example.com', 'ADMIN'),
('user1', '$2a$10$rDkPvvAFV6GgJkKqX3q3/.KqX3q3/.KqX3q3/.KqX3q3/.KqX3q3/', 'user1@example.com', 'USER'),
('user2', '$2a$10$rDkPvvAFV6GgJkKqX3q3/.KqX3q3/.KqX3q3/.KqX3q3/.KqX3q3/', 'user2@example.com', 'USER');

INSERT INTO classrooms (building_type, room_number, capacity, is_available, current_status) VALUES
('教学楼A', '101', 50, 1, 'available'),
('教学楼A', '102', 60, 1, 'available'),
('教学楼B', '201', 40, 1, 'available'),
('教学楼B', '202', 45, 1, 'available');

INSERT INTO resources (resource_type, resource_name, location, capacity, description, is_available) VALUES
('会议室', '会议室A', '行政楼1层', 20, '小型会议室', 1),
('会议室', '会议室B', '行政楼2层', 30, '中型会议室', 1),
('实验室', '物理实验室', '实验楼1层', 25, '物理实验教学', 1),
('实验室', '化学实验室', '实验楼2层', 25, '化学实验教学', 1);

INSERT INTO booking_records (resource_id, user_id, start_time, end_time, status) VALUES
(1, 2, datetime('now', '+1 day'), datetime('now', '+1 day', '+2 hours'), 'pending'),
(2, 2, datetime('now', '+2 days'), datetime('now', '+2 days', '+3 hours'), 'approved'),
(3, 3, datetime('now', '+3 days'), datetime('now', '+3 days', '+4 hours'), 'pending'); 