-- 创建数据库
CREATE DATABASE IF NOT EXISTS campus_resource DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE campus_resource;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50),
    student_id VARCHAR(20),
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 资源类型表
CREATE TABLE IF NOT EXISTS resource_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 资源表
CREATE TABLE IF NOT EXISTS resource (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type_id BIGINT NOT NULL,
    location VARCHAR(200) NOT NULL,
    description TEXT,
    capacity INT,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (type_id) REFERENCES resource_type(id)
);

-- 预约表
CREATE TABLE IF NOT EXISTS booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resource_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    purpose TEXT,
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (resource_id) REFERENCES resource(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 插入示例数据

-- 插入用户数据
INSERT INTO user (username, password, real_name, student_id, phone, email) VALUES
('student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU3VxqW', '张三', '2021001', '13800138001', 'zhangsan@example.com'),
('student2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU3VxqW', '李四', '2021002', '13800138002', 'lisi@example.com'),
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU3VxqW', '管理员', 'admin001', '13800138000', 'admin@example.com');

-- 插入资源类型
INSERT INTO resource_type (name, description) VALUES
('CLASSROOM', '普通教室'),
('STUDY_ROOM', '自习室'),
('SPORTS_FACILITY', '体育设施'),
('LABORATORY', '实验室'),
('MEETING_ROOM', '会议室'),
('ART_ROOM', '艺术室');

-- 插入资源数据
INSERT INTO resource (name, type_id, location, description, capacity, available) VALUES
-- 教室
('A101', 1, '教学楼A区1层101室', '多媒体教室，配备投影仪和音响设备', 50, true),
('A102', 1, '教学楼A区1层102室', '普通教室，配备黑板和投影仪', 45, true),
('B201', 1, '教学楼B区2层201室', '阶梯教室，配备多媒体设备', 100, false),

-- 自习室
('图书馆自习室1', 2, '图书馆1层', '安静的自习环境，配备空调和WiFi', 80, true),
('图书馆自习室2', 2, '图书馆2层', '安静的自习环境，配备空调和WiFi', 80, false),
('教学楼自习室', 2, '教学楼C区1层', '24小时开放的自习室', 60, true),

-- 体育设施
('篮球场1', 3, '体育馆1层', '标准室内篮球场', 20, true),
('羽毛球场1', 3, '体育馆2层', '标准羽毛球场', 4, false),
('乒乓球室', 3, '体育馆1层', '配备4张乒乓球台', 8, true),

-- 实验室
('物理实验室1', 4, '实验楼1层101室', '基础物理实验设备', 30, true),
('化学实验室1', 4, '实验楼1层102室', '基础化学实验设备', 30, false),
('计算机实验室1', 4, '实验楼2层201室', '配备高性能计算机', 40, true),

-- 会议室
('会议室A', 5, '行政楼1层', '小型会议室，配备投影仪', 20, true),
('会议室B', 5, '行政楼2层', '中型会议室，配备视频会议系统', 30, false),
('会议室C', 5, '行政楼3层', '大型会议室，配备音响系统', 50, true),

-- 艺术室
('音乐室1', 6, '艺术楼1层', '配备钢琴和音响设备', 20, true),
('美术室1', 6, '艺术楼2层', '配备画架和绘画工具', 30, false),
('舞蹈室1', 6, '艺术楼3层', '配备镜子和音响设备', 25, true);

-- 插入预约数据
INSERT INTO booking (resource_id, user_id, start_time, end_time, purpose, status) VALUES
(2, 1, '2024-03-20 09:00:00', '2024-03-20 11:00:00', '课程教学', 'APPROVED'),
(5, 2, '2024-03-20 14:00:00', '2024-03-20 16:00:00', '自习', 'APPROVED'),
(9, 1, '2024-03-21 15:00:00', '2024-03-21 17:00:00', '篮球训练', 'PENDING'),
(12, 2, '2024-03-22 10:00:00', '2024-03-22 12:00:00', '化学实验', 'APPROVED'),
(15, 1, '2024-03-23 09:00:00', '2024-03-23 11:00:00', '项目会议', 'APPROVED'); 