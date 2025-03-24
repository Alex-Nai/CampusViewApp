-- 创建资源表
CREATE TABLE IF NOT EXISTS resources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    location VARCHAR(255),
    capacity INT,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'available'
);

-- 创建预约记录表
CREATE TABLE IF NOT EXISTS booking_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    booking_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    remarks TEXT,
    FOREIGN KEY (resource_id) REFERENCES resources(id)
); 